package ChuyenNganh.Seafood.Payload.Request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 20, message = "Tên đăng nhập phải từ 3 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Size(max = 50, message = "Email không vượt quá 50 kí tự")
    @Email(message = "Vui lòng nhập đúng định dạng Email")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 40, message = "Mật khẩu phải từ 6 đến 40 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(min = 4, max = 50, message = "Họ và tên phải từ 4 đến 50 kí tự")
    private String fullname;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Vui lòng nhập đúng số điện thoại")
    private String phone;
}