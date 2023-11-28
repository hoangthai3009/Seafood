package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Entity.compositeKey.BillDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface IBillDetailRepository extends JpaRepository<BillDetail, BillDetailKey> {
    @Query("SELECT bd FROM BillDetail bd " +
            "JOIN Bill b ON b.id = bd.id.billId " +
            "JOIN User u ON u.id = b.user.id " +
            "WHERE u.id = :userId")
    List<BillDetail> findAllBillDetailByUser(@Param("userId") Long userId);

    @Query("SELECT bd FROM BillDetail bd " +
            "WHERE bd.seafood.id = :productId AND bd.bill.id = :billId")
    BillDetail findBillDetailByProduct(@Param("productId") Long productId, @Param("billId") Long billId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bill_detail (seafood_id, bill_id, amount) " +
            "VALUES (?1, ?2, ?3)", nativeQuery = true)
    void addProductToBill(Long productId, Long billId, int amount);

}
