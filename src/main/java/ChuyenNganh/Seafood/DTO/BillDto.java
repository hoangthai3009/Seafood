package ChuyenNganh.Seafood.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BillDto {
    private Long billId;
    private BigDecimal totalPrice;
    private LocalDate createdAt;
    private String fullname;
    private String address;
    private String phone;
    private String note;
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
