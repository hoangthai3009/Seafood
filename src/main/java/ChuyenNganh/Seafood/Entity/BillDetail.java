package ChuyenNganh.Seafood.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class BillDetail {

    @EmbeddedId
    private BillDetailId id;

    @ManyToOne
    @MapsId("seafoodId")
    @JoinColumn(name = "seafood_id")
    private Seafood seafood;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}

