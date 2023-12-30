package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import ChuyenNganh.Seafood.Security.Services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequestMapping

@Controller
public class UIController {
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "User/login";
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

    @Autowired
    private BillService billService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile/{userId}")
    public String profile(Model model, @PathVariable Long userId) {
        List<Bill> orders = billService.getBillByUserId(userId);
        model.addAttribute("orders", orders);
        return "User/profile";
    }

    @GetMapping("/promotions")
    public String promotions(){return "Home/promotions";}
}
