package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.EProvider;
import ChuyenNganh.Seafood.Entity.ERole;
import ChuyenNganh.Seafood.Entity.Role;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Payload.Request.LoginRequest;
import ChuyenNganh.Seafood.Payload.Request.RegisterRequest;
import ChuyenNganh.Seafood.Payload.Response.MessageResponse;
import ChuyenNganh.Seafood.Repositories.IRoleRepository;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import ChuyenNganh.Seafood.Security.Jwt.JwtUtils;
import ChuyenNganh.Seafood.Security.Services.EmailSenderService;
import ChuyenNganh.Seafood.Security.Services.UserDetailsImpl;
import ChuyenNganh.Seafood.Security.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JwtUtils jwtUtils;

    @RestControllerAdvice
    public static class ExceptionHandlerAdvice {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            System.out.println(errors);
            return errors;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail()
        );
        if (user.isEmpty() || !user.get().isEnabled()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Sai thông tin đăng nhập hoặc Tài khoản chưa được xác thực");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", userDetails.getId());
        responseBody.put("fullname", userDetails.getFullname());
        responseBody.put("username", userDetails.getUsername());
        responseBody.put("email", userDetails.getEmail());
        responseBody.put("address", userDetails.getAddress());
        responseBody.put("phone", userDetails.getPhone());
        responseBody.put("roles", roles);
        responseBody.put("status", 0);
        userRepository.updateUserStatusToOnline(userDetails.getUsername());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("errorUsername", "Tên đăng nhập đã tồn tại"));
            }
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("errorEmail", "Email đã được đăng ký"));
            }
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setFullname(registerRequest.getFullname());
            user.setPhone(registerRequest.getPhone());Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Role is not found."));
            user.getRoles().add(role);
            user.setEnabled(false);
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setProvider(EProvider.LOCAL);

            String confirmationToken = UUID.randomUUID().toString();
            user.setToken(confirmationToken);

            userRepository.save(user);

            String confirmationUrl = "http://localhost:8080/confirm?token=" + confirmationToken;
            try {
                emailSenderService.sendEmail(user.getEmail(), "Xác nhận đăng ký", confirmationUrl);
            } catch (Exception e) {
                userRepository.delete(user);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error sending confirmation email. Check logs for details."));
            }

            return ResponseEntity.ok().body(new MessageResponse("User registered successfully! Please check your email for confirmation."));
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during user registration."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userRepository.updateUserStatusToOffline(userDetails.getUsername());
        }
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}