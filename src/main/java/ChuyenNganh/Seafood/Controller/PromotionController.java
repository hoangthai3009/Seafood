package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.Promotion;
import ChuyenNganh.Seafood.Security.Services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/promotions")
    public String getAllPromotions(Model model) {
        List<Promotion> promotions = promotionService.getAllPromotions();
        model.addAttribute("promotions", promotions);
        return "Admin/promotion/list-promotion";
    }

    @GetMapping("/create-promotion")
    public String showCreateForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "Admin/promotion/create-promotion";
    }

    @PostMapping("/create-promotion")
    public String createPromotion(@ModelAttribute Promotion promotion, @RequestParam("code") String promotionCode) {
        promotion.setCode(promotionCode);
        promotionService.savePromotion(promotion);
        return "redirect:/admin/promotions";
    }
    @GetMapping("/edit-promotion/{code}")
    public String showEditForm(@PathVariable("code") String code, Model model) {
        Optional<Promotion> promotionOpt = promotionService.getPromotionByCode(code);
        if (promotionOpt.isEmpty()) {
            return "redirect:/admin/promotions"; // or some error page
        }
        model.addAttribute("promotion", promotionOpt.get());
        return "Admin/promotion/edit-promotion";
    }

    @PostMapping("/edit-promotion")
    public String editPromotion(@ModelAttribute Promotion promotion) {
        promotionService.savePromotion(promotion);
        return "redirect:/admin/promotions";
    }

    @GetMapping("/delete-promotion/{code}")
    public String deletePromotion(@PathVariable String code) {
        promotionService.deletePromotion(code);
        return "redirect:/admin/promotions";
    }
}
