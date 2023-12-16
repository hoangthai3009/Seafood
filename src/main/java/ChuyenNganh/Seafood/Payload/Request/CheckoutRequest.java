package ChuyenNganh.Seafood.Payload.Request;

import ChuyenNganh.Seafood.Entity.User;
import lombok.Data;

import java.util.List;
@Data
public class CheckoutRequest {

    private double totalPrice;
    private String note;
    private String address;
    private User user;
    private List<BillDetailRequest> billDetails;
    private  Long userId;
}