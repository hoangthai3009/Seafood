package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Security.Services.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/Authenticate")
    @PreAuthorize("isAuthenticated()")
    public String authenticate() {
        return "Authenticate Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/info")
    public String getUserInfo() {
        // Lấy thông tin chi tiết của người dùng từ SecurityContextHolder
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Lấy danh sách các role của người dùng
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Tạo một chuỗi thông tin với tên role của người dùng
        String userInfo = "User: " + userDetails.getUsername() + ", Roles: " + String.join(", ", roles);


        return userInfo;
    }
}

