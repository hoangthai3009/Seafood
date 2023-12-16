package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Security.Services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CartApiController {
    @Autowired
    private CartService cartService;

    @GetMapping("/check-login")
    public Map<String, Object> checkAuthentication(Authentication authenticatio) {
        boolean isAuthenticated = authenticatio != null;

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", isAuthenticated);
        response.put("userDetails", authenticatio); // Include userDetails for debugging

        return response;
    }
    @GetMapping("/cartItemCount")
    public ResponseEntity<Integer> getCartItemCount(HttpSession session) {
        int count = cartService.getSumQuantity(session);
        return ResponseEntity.ok(count);
    }
}
