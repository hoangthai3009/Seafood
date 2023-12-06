package ChuyenNganh.Seafood.Entity;

import ChuyenNganh.Seafood.Validators.Annotation.ValidCategoryId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "seafood")
public class Seafood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Tên không được để trống")
    @Size(max = 50, min = 1, message = "Tên không được vượt quá 50 kí tự")
    private String name;

    @Column(name = "origin")
    @NotEmpty(message = "Xuất xứ không được để trống")
    @Size(max = 50, min = 1, message = "Xuất xứ không được vượt quá 50 kí tự")
    private String origin;

    @Column(name = "description")
    @NotEmpty(message = "Mô tả không được để trống")
    @Size(max = 255, min = 1, message = "Mô tả không được vượt quá 255 kí tự")
    private String description;

    @Column(name = "price")
    @NotNull(message = "Giá tiền không được để trống")
    private Double price;

    @Column(name ="main_image")
    private String mainImage;
    @Column(name="extra_image1")
    private String extraImage1;
    @Column(name="extra_image2")
    private String extraImage2;
    @Column(name="extra_image3")
    private String extraImage3;

    @Column(name = "quantity")
    @NotNull(message = "Số lượng không được để trống")
    private int quantity;

    @Column(name = "manufacturing_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sản xuất không được để trống")
    private Date manufacturing_date;

    @Column(name = "unit")
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ValidCategoryId
    @ToString.Exclude
    private Category category;
}