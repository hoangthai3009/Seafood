package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Entity.BillDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBillDetailRepository extends JpaRepository<BillDetail, BillDetailId> {
    List<BillDetail> findBillDetailsByBillId(Long billId);

}
