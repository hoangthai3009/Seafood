package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Repositories.IBillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BillDetailService {
    @Autowired
    private IBillDetailRepository billDetailRepository;

    public List<BillDetail> getAllBillDetail(Long userId) {
        return billDetailRepository.findAllBillDetailByUser(userId);
    }

    public BillDetail getBillDetailByProduct(Long productId, Long billId) {
        return billDetailRepository.findBillDetailByProduct(productId, billId);
    }

    public void saveBillDetail(BillDetail billDetail) {
        billDetailRepository.save(billDetail);
    }

    public void addProductToBill(Long productId, Long billId, int quantity) {
        billDetailRepository.addProductToBill(productId, billId, quantity);
    }

}
