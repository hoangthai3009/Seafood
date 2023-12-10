package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    public static final String CART_SESSION_KEY = "cart";

    public void addToCart(HttpSession session, Long seafoodId, String seafoodName, Double price, String image, String categoryName, int quantity) {
        List<CartItem> cartItems = getOrCreateCart(session);

        boolean seafoodExists = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getSeafoodId().equals(seafoodId)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                seafoodExists = true;
                break;
            }
        }

        if (!seafoodExists) {
            CartItem cartItem = new CartItem();
            cartItem.setSeafoodId(seafoodId);
            cartItem.setSeafoodName(seafoodName);
            cartItem.setPrice(price);
            cartItem.setImage(image);
            cartItem.setCategoryName(categoryName);
            cartItem.setQuantity(quantity);
            cartItems.add(cartItem);
        }

        session.setAttribute(CART_SESSION_KEY, cartItems);
    }

    public int getTotalQuantity(HttpSession session) {
        List<CartItem> cartItems = getOrCreateCart(session);
        return cartItems.size();
    }


    public void updateCart(HttpSession session, Long seafoodId, int quantity) {
        List<CartItem> cartItems = getOrCreateCart(session);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getSeafoodId().equals(seafoodId)) {
                cartItem.setQuantity(quantity);
                break;
            }
        }

        session.setAttribute(CART_SESSION_KEY, cartItems);
    }

    public List<CartItem> viewCart(HttpSession session) {
        return getOrCreateCart(session);
    }

    private List<CartItem> getOrCreateCart(HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cartItems);
        }
        return cartItems;
    }
}
