package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Entity.BillDetailId;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Payload.Request.BillDetailRequest;
import ChuyenNganh.Seafood.Payload.Request.CheckoutRequest;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import ChuyenNganh.Seafood.Security.Services.BillDetailService;
import ChuyenNganh.Seafood.Security.Services.BillService;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    IUserRepository userRepository;
    @Autowired
    private VNPayController vnpayController;
    @PostMapping
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest checkoutRequest, HttpServletRequest request) {
        try {
            if ("VNPay".equals(checkoutRequest.getPaymentMethod())) {
                String paymentUrl = String.valueOf(vnpayController.createPayment(request, checkoutRequest.getTotalPrice()));
                // Chuyển hướng người dùng đến URL VNPay
                return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
            }

            User user = userRepository.findById(checkoutRequest.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            Bill newBill = new Bill();
            newBill.setUser(user);
            newBill.setCreatedAt(new Date());
            newBill.setTotalPrice(checkoutRequest.getTotalPrice());
            newBill.setNote(checkoutRequest.getNote());
            newBill.setAddress(checkoutRequest.getAddress());
            newBill.setDiscount(checkoutRequest.getDiscount());
            newBill.setTotalPay(checkoutRequest.getTotalPay());
            newBill.setPaymentMethod(checkoutRequest.getPaymentMethod());
            newBill.setTotalPaid(checkoutRequest.getTotalPaid());
            newBill.setPaidPercentage(checkoutRequest.getPaidPercentage());

            Bill savedBill = billService.saveBill(newBill);

            for (BillDetailRequest detailRequest : checkoutRequest.getBillDetails()) {
                BillDetail billDetail = new BillDetail();

                BillDetailId billDetailId = new BillDetailId();
                billDetailId.setBillId(savedBill.getId());
                billDetailId.setSeafoodId(detailRequest.getSeafood().getId());

                billDetail.setId(billDetailId);

                billDetail.setBill(savedBill);
                billDetail.setSeafood(detailRequest.getSeafood());
                billDetail.setQuantity(detailRequest.getQuantity());
                billDetail.setPrice(detailRequest.getPrice());

                billDetailService.saveBillDetail(billDetail);
            }

            return new ResponseEntity<>("THÀNH CÔNG !", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during checkout: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}