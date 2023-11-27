package ChuyenNganh.Seafood.Entity;

import ChuyenNganh.Seafood.Entity.compositeKey.BillDetailKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bill_detail")
public class BillDetail {
    @EmbeddedId
    private BillDetailKey id;

    @ManyToOne
    @MapsId("seafoodId")
    @JoinColumn(name = "seafood_id")
    private Seafood seafood;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(name = "amount")
    private Long amount;
}
