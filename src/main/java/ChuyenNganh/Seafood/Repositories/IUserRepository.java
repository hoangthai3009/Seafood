package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    User findByToken(String token);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_roles (user_id, role_id) " +
            "VALUES (?1, ?2)", nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);
    @Query(value = "SELECT r.name FROM Role r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "INNER JOIN user u ON u.id = ur.user_id " +
            "WHERE u.username = :username", nativeQuery = true)
    String[] getRolesOfUser(@Param("username") String username);
    @Query(value = "SELECT r.description FROM Role r INNER JOIN user_roles ur " +
            "ON r.id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
    String[] getRolesOfUser(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_roles WHERE user_id = ?1 AND role_id = ?2", nativeQuery = true)
    void removeRoleFromUser(Long userId, Long roleId);

}
