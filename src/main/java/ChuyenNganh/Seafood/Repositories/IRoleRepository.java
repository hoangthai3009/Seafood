package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.ERole;
import ChuyenNganh.Seafood.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}