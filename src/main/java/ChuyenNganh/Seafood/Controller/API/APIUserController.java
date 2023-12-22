package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Security.Services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class APIUserController {
    @Autowired
    private BillService billService;
    @GetMapping("/api/userOrder/{userId}")
    public ResponseEntity<List<Bill>> getUserOrders(@PathVariable Long userId) {
        List<Bill> orders = billService.getBillByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    
}
