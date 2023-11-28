package ChuyenNganh.Seafood.Payload.Request;

import lombok.Data;

@Data
public class CartItemRequest {

    private Long seafoodId;
    private int quantity;
}
