package ChuyenNganh.Seafood.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "total_price")
    private double totalPrice;

    private String note;

    private String address;

    private double discount;

    @Column(name = "total_pay")
    private double totalPay;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_percentage")
    private double paidPercentage;

    @Column(name = "total_paid")
    private double totalPaid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private Set<BillDetail> billDetails;

    public void removeBillDetail(BillDetail billDetail) {
        this.billDetails.remove(billDetail);
        billDetail.setBill(null); // Đặt Bill của BillDetail này thành null
    }
}

