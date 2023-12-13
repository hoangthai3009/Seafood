package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {
}