package ChuyenNganh.Seafood.Entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class BillDetailKey {
    @Column(name = "seafood_id")
    private Long seafoodId;

    @Column(name = "bill_id")
    private Long billId;
}
