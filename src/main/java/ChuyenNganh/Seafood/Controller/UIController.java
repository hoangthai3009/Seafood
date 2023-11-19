package ChuyenNganh.Seafood.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/signin")
    public String showSignInPage() {
        return "User/signin";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "User/signup";
    }
}
