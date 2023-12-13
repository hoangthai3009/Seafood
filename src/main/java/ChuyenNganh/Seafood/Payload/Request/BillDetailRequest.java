package ChuyenNganh.Seafood.Payload.Request;

import ChuyenNganh.Seafood.Entity.Seafood;
import lombok.Data;

@Data
public class BillDetailRequest {

    private Seafood seafood;
    private int quantity;
    private double price;
}

