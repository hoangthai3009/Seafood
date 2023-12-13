package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Security.Services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartApiController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cartItemCount")
    public ResponseEntity<Integer> getCartItemCount(HttpSession session) {
        int count = cartService.getSumQuantity(session);
        return ResponseEntity.ok(count);
    }
}
