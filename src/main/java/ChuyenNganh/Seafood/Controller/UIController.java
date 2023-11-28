package ChuyenNganh.Seafood.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "User/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "User/register";
    }
}
