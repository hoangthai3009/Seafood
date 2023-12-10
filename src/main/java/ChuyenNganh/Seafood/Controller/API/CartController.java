package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.CartItem;
import ChuyenNganh.Seafood.Payload.Request.CartItemRequest;
import ChuyenNganh.Seafood.Security.Services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemRequest cartItemRequest, HttpSession session) {
        Long seafoodId = cartItemRequest.getSeafoodId();
        String seafoodName = cartItemRequest.getSeafoodName();
        Double price = cartItemRequest.getPrice();
        String image = cartItemRequest.getImage();
        String categoryName = cartItemRequest.getCategoryName();
        int quantity = cartItemRequest.getQuantity();

        cartService.addToCart(session, seafoodId, seafoodName, price, image, categoryName, quantity);

        return ResponseEntity.ok("Seafood added to cart successfully");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCart(@RequestBody CartItemRequest cartItemRequest, HttpSession session) {
        Long seafoodId = cartItemRequest.getSeafoodId();
        int quantity = cartItemRequest.getQuantity();

        cartService.updateCart(session, seafoodId, quantity);

        return ResponseEntity.ok("Seafood updated to cart successfully");
    }

    @GetMapping("/view")
    public ResponseEntity<List<CartItem>> viewCart(HttpSession session) {
        List<CartItem> cartItems = cartService.viewCart(session);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> viewQuantityCart(HttpSession session) {
        int quantity = cartService.getTotalQuantity(session);
        return ResponseEntity.ok(quantity);
    }
}
