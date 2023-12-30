package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Promotion;
import ChuyenNganh.Seafood.Repositories.IPromotionRepository;
import ChuyenNganh.Seafood.Security.Services.PromotionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private IPromotionRepository promotionRepository;
    @PostMapping("/apply/{code}")
    public ResponseEntity<?> applyPromotion(@PathVariable String code, HttpSession session) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(code);
        if (promotionOptional.isEmpty())
            return ResponseEntity.badRequest().body("Mã khuyến mãi không tồn tại");
        Promotion promotion = promotionOptional.get();
        if (promotion.getExpired_day() == null || promotion.getExpired_day().toInstant().isBefore(Instant.now()))
            return ResponseEntity.badRequest().body("Mã khuyến mãi đã hết hạn");
        if (promotion.getQuantity() <= 0)
            return ResponseEntity.badRequest().body("Mã khuyến mãi đã hết số lượng");
        Double totalPrice = (Double) session.getAttribute("totalPrice");
        if (totalPrice == null || totalPrice < 0)
            return ResponseEntity.badRequest().body("Giá trị tổng cộng không hợp lệ");
        double totalDiscount = totalPrice * promotion.getDiscount();
        double totalPay = totalPrice - totalDiscount;
        if (totalPay < 0)
            return ResponseEntity.badRequest().body("Tổng số tiền phải trả không thể âm");
        session.setAttribute("totalDiscount", totalDiscount);
        session.setAttribute("totalPay", totalPay);
        Map<String, Double> result = new HashMap<>();
        result.put("totalDiscount", totalDiscount);
        result.put("totalPay", totalPay);
        result.put("totalPrice", totalPrice);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/find")
    public ResponseEntity<Optional<Promotion>> findPromotionByCode(@RequestParam String code) {
        Optional<Promotion> promotionFound = promotionService.getPromotionByCode(code);
        if(promotionFound.isPresent() && promotionFound.get().getExpired_day().toInstant().isAfter(Instant.now())){
            return new ResponseEntity<>(promotionFound, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/decrement/{code}")
    public ResponseEntity<?> decrementPromotionQuantity(@PathVariable String code) {
        Optional<Promotion> promotionOptional = promotionRepository.findById(code);
        if (promotionOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Mã khuyến mãi không tồn tại");
        }
        Promotion promotion = promotionOptional.get();
        if (promotion.getQuantity() > 0) {
            promotion.setQuantity(promotion.getQuantity() - 1);
            promotionRepository.save(promotion);
            return ResponseEntity.ok().body("Số lượng mã khuyến mãi đã được cập nhật");
        } else {
            return ResponseEntity.badRequest().body("Mã khuyến mãi đã hết số lượng");
        }
    }
}