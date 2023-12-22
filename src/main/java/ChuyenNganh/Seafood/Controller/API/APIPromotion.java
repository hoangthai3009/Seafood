package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Promotion;
import ChuyenNganh.Seafood.Security.Services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
public class APIPromotion {

    private final PromotionService promotionService;

    @Autowired
    public APIPromotion(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        Promotion createdPromotion = promotionService.savePromotion(promotion);
        return new ResponseEntity<>(createdPromotion, HttpStatus.CREATED);
    }
}