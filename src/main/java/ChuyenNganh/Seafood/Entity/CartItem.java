package ChuyenNganh.Seafood.Entity;

import lombok.Data;

@Data
public class CartItem {
    private Long seafoodId;
    private String seafoodName;
    private double price;
    private String image;
    private String categoryName;
    private int quantity;
}
