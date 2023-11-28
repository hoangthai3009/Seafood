package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private SeafoodService seafoodService;
    @GetMapping
    public String index(Pageable pageable, Model model) {
        Page<Seafood> seafoods = seafoodService.getAllSeafoods(pageable);
        model.addAttribute("seafoods", seafoods);
        return "home/index";
    }
}
