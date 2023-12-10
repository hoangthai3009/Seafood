package ChuyenNganh.Seafood.Payload.Request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long seafoodId;
    private String seafoodName;
    private double price;
    private String image;
    private String categoryName;
    private int quantity;
}
