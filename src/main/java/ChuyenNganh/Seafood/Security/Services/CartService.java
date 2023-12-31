package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.DAOS.Cart;
import ChuyenNganh.Seafood.Entity.CartItem;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    public static final String CART_SESSION_KEY = "cart";
    public Cart getCart(@NotNull HttpSession session) {
        return Optional.ofNullable((Cart) session.getAttribute(CART_SESSION_KEY))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    session.setAttribute(CART_SESSION_KEY, cart);
                    return cart;
                });
    }

    public Page<CartItem> getCartPage(HttpSession session, Pageable pageable) {
        Cart cart = getCart(session);
        List<CartItem> cartItems = cart.getCartItems();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cartItems.size());

        List<CartItem> pageItems = cartItems.subList(start, end);

        return new PageImpl<>(pageItems, pageable, cartItems.size());
    }

    public void updateCartModel(@NotNull HttpSession session, Cart cart) {
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeCart(@NotNull HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public int getSumQuantity(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public double getSumPrice(@NotNull HttpSession session) {
        return getCart(session).getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
