package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPromotionRepository extends JpaRepository<Promotion, String> {
}
