package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Entity.BillDetailId;
import ChuyenNganh.Seafood.Payload.Request.BillDetailRequest;
import ChuyenNganh.Seafood.Payload.Request.CheckoutRequest;
import ChuyenNganh.Seafood.Security.Services.BillDetailService;
import ChuyenNganh.Seafood.Security.Services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/checkout")
public class APIBillController {

    private final BillService billService;
    private final BillDetailService billDetailService;

    @Autowired
    public APIBillController(BillService billService, BillDetailService billDetailService) {
        this.billService = billService;
        this.billDetailService = billDetailService;
    }

    @PostMapping
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            // Create a new Bill
            Bill newBill = new Bill();
            newBill.setCreatedAt(new Date());
            newBill.setTotalPrice(checkoutRequest.getTotalPrice());
            newBill.setNote(checkoutRequest.getNote());
            newBill.setAddress(checkoutRequest.getAddress());
            // Save the Bill
            Bill savedBill = billService.saveBill(newBill);

            // Create BillDetails
            for (BillDetailRequest detailRequest : checkoutRequest.getBillDetails()) {
                BillDetail billDetail = new BillDetail();

                // Create BillDetailId
                BillDetailId billDetailId = new BillDetailId();
                billDetailId.setBillId(savedBill.getId());  // Assuming getId() returns the generated ID of the savedBill
                billDetailId.setSeafoodId(detailRequest.getSeafood().getId());  // Assuming getId() returns the ID of the seafood

                billDetail.setId(billDetailId);

                billDetail.setBill(savedBill);
                billDetail.setSeafood(detailRequest.getSeafood());
                billDetail.setQuantity(detailRequest.getQuantity());
                billDetail.setPrice(detailRequest.getPrice());
                // Save the BillDetail
                billDetailService.saveBillDetail(billDetail);
            }

            return new ResponseEntity<>("Checkout successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during checkout: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}