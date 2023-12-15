package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c, Seafood s WHERE c.seafood.id = s.id AND s.id = ?1")
    List<Comment> findCommentBySeafoodId(Long productId);
}
