package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UIController {
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "User/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "User/register";
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        User user = userRepository.findByToken(token);

        if (user == null) {
            return "Error/404";
        }

        user.setEnabled(true);
        user.setToken(null);
        userRepository.save(user);

        return "User/registration-success";
    }
    @GetMapping("/seafood/list")
    public String listSeafoods(){return "Seafood/list";}

    @GetMapping("/seafood/{id}")
    public String getSeafoodDetail() {
        return "Seafood/detail";
    }

}
