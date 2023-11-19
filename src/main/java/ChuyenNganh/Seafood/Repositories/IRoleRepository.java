package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.roleName = ?1")
    Role findByRoleName(String roleName);
    @Query("SELECT r.roleId FROM Role r WHERE r.roleName = ?1")
    Long getRoleIdByRoleName(String roleName);
}
