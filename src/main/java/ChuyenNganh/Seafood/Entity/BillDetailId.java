package ChuyenNganh.Seafood.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class BillDetailId implements Serializable {

    private Long seafoodId;

    private Long billId;
}
