package ChuyenNganh.Seafood.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
public class Promotion {
    @Id
    private String code;
    private int quantity;
    private String name;
    private String description;
    private double discount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expired_day;
}
