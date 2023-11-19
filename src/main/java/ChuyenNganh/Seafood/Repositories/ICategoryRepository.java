package ChuyenNganh.Seafood.Repositories;


import ChuyenNganh.Seafood.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}