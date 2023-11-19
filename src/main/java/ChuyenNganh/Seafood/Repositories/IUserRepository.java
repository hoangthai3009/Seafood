package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_id) " + "VALUES (?1, ?2)", nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);

    @Query("SELECT u.userId FROM User u WHERE u.username = ?1")
    Long getUserIdByUsername(String username);

    @Query(value = "SELECT r.role_name FROM Role r INNER JOIN user_role ur " + "ON r.role_id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
    String[] getRolesOfUser(Long userId);

    @Query(value = "SELECT r.role_name FROM Role r " +
            "INNER JOIN user_role ur ON r.role_id = ur.role_id " +
            "INNER JOIN user u ON u.user_id = ur.user_id " +
            "WHERE u.username = :username", nativeQuery = true)
    String[] getRolesOfUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_role WHERE user_id = ?1 AND role_id = ?2", nativeQuery = true)
    void removeRoleFromUser(Long userId, Long roleId);
}
