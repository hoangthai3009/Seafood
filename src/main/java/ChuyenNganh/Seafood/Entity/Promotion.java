package ChuyenNganh.Seafood.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Entity
@Data
public class Promotion {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String code;
    private int quantity;
    private String name;
    private String description;
    private double discount;
}
