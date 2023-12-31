package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Promotion;
import ChuyenNganh.Seafood.Repositories.IPromotionRepository;
import ChuyenNganh.Seafood.Security.Services.CartService;
import ChuyenNganh.Seafood.Security.Services.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CartApiController {
    @Autowired
    private CartService cartService;

    @GetMapping("/check-login")
    public ResponseEntity<?> checkAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getId();
            String fullname = userDetails.getFullname();
            String email = userDetails.getEmail();
            String phone = userDetails.getPhone();
            String address = userDetails.getAddress();
            String username = userDetails.getUsername();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("authenticated", true);
            responseMap.put("userId", userId);
            responseMap.put("fullname", fullname);
            responseMap.put("email", email);
            responseMap.put("phone", phone);
            responseMap.put("username", username);
            if (address != null)
                responseMap.put("address", address);
            return ResponseEntity.ok(responseMap);

        }
        return ResponseEntity.ok(Map.of("authenticated", false));
    }
    @GetMapping("/cartItemCount")
    public ResponseEntity<Integer> getCartItemCount(HttpSession session) {
        int count = cartService.getSumQuantity(session);
        return ResponseEntity.ok(count);
    }
    @GetMapping("/api/clearCart")
    public void clearCartSession(HttpSession session) {
        session.removeAttribute("cart");
    }
}
