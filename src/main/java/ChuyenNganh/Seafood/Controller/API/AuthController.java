package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Constants.Provider;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
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


    @GetMapping("/check-login")
    public ResponseEntity<?> checkLoginStatus(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Collections.singletonMap("roles", roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
            }

            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
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
            user.setProvider(Provider.LOCAL.value);

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
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}