package ChuyenNganh.Seafood.Payload.Request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(max = 50)
    private String fullname;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;
}