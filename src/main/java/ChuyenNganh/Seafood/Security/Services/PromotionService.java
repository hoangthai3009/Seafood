package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.Promotion;
import ChuyenNganh.Seafood.Repositories.IPromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    private final IPromotionRepository promotionRepository;

    @Autowired
    public PromotionService(IPromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Optional<Promotion> getPromotionByCode(String code) {
        return promotionRepository.findById(code);
    }

    public Promotion savePromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }


    public void deletePromotion(String id) {
        promotionRepository.deleteById(id);
    }
}
