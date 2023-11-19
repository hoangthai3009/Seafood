package ChuyenNganh.Seafood.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Size(max = 50, message = "Tên Role không vượt quá 50 kí tự")
    @NotBlank(message = "Tên Role không được để trống")
    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @Size(max = 255, message = "Mô tả không vượt quá 255 kí tự")
    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}