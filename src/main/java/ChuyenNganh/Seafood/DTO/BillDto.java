package ChuyenNganh.Seafood.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class BillDto {
    private Long billId;
    private BigDecimal totalPrice;
    private Date createdAt;
    private String name;
}
