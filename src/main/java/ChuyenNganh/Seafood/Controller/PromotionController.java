package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.Promotion;
import ChuyenNganh.Seafood.Security.Services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public String getAllPromotions(Model model) {
        List<Promotion> promotions = promotionService.getAllPromotions();
        model.addAttribute("promotions", promotions);
        return "promotion/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "promotion/create";
    }

    @PostMapping("/new")
    public String createPromotion(@ModelAttribute Promotion promotion) {
        promotionService.createPromotion(promotion);
        return "redirect:/promotions";
    }

    @GetMapping("/{code}/edit")
    public String showUpdateForm(@PathVariable String code, Model model) {
        Optional<Promotion> promotion = promotionService.getPromotionByCode(code);
        promotion.ifPresent(value -> model.addAttribute("promotion", value));
        return "promotion/edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePromotion(@PathVariable String id, @ModelAttribute Promotion updatedPromotion) {
        promotionService.updatePromotion(id, updatedPromotion);
        return "redirect:/promotions";
    }

    @GetMapping("/{id}/delete")
    public String deletePromotion(@PathVariable String id) {
        promotionService.deletePromotion(id);
        return "redirect:/promotions";
    }
}
