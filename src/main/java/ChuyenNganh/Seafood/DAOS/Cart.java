package ChuyenNganh.Seafood.DAOS;
import ChuyenNganh.Seafood.Entity.CartItem;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();

    public void addItems(CartItem item) {
        boolean isExist = cartItems.stream()
                .filter(i -> Objects.equals(i.getSeafoodId(), item.getSeafoodId()))
                .findFirst()
                .map(i -> {
                    i.setQuantity(i.getQuantity() + item.getQuantity());
                    return true;
                })
                .orElse(false);
        if (!isExist) {
            cartItems.add(item);
        }
    }

    public void removeItems(Long seafoodId) {
        cartItems.removeIf(item -> Objects.equals(item.getSeafoodId(), seafoodId));
    }

    public void updateItems(Long seafoodId, int quantity) {
        cartItems.stream()
                .filter(item -> Objects.equals(item.getSeafoodId(), seafoodId))
                .forEach(item -> item.setQuantity(quantity));
    }
}
