package ChuyenNganh.Seafood.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String usernameOrEmail;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}