package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Entity.BillDetailId;
import ChuyenNganh.Seafood.Repositories.IBillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillDetailService {

    private final IBillDetailRepository billDetailRepository;

    @Autowired
    public BillDetailService(IBillDetailRepository billDetailRepository) {
        this.billDetailRepository = billDetailRepository;
    }

    public List<BillDetail> getAllBillDetails() {
        return billDetailRepository.findAll();
    }

    public Optional<BillDetail> getBillDetailById(BillDetailId id) {
        return billDetailRepository.findById(id);
    }

    public BillDetail saveBillDetail(BillDetail billDetail) {
        return billDetailRepository.save(billDetail);
    }

    public void deleteBillDetail(BillDetailId id) {
        billDetailRepository.deleteById(id);
    }
}
