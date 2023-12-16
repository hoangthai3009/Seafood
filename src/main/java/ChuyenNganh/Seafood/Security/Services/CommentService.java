package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.Comment;
import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Repositories.ICommentRepository;
import ChuyenNganh.Seafood.Repositories.ISeafoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private ISeafoodRepository seafoodRepository;

    public List<Comment> getCommentBySeafoodId(Long id) {
        List<Comment> comment = commentRepository.findCommentBySeafoodId(id);
        return comment;
    }

    public Comment addComment(Long seafoodId, Comment comment) {
        // Kiểm tra xem hải sản có tồn tại không
        Seafood seafood = seafoodRepository.findById(seafoodId).orElse(null);
        if (seafood == null) {
            return null;
        }
        comment.setSeafood(seafood);
        return commentRepository.save(comment);
    }
    public Comment editComment(Long commentId, Long userId, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null && existingComment.getUser().getId().equals(userId)) {
            existingComment.setContent(updatedComment.getContent());
            return commentRepository.save(existingComment);
        }
        return null;
    }

    public boolean deleteComment(Long commentId, Long userId) {
        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null && existingComment.getUser().getId().equals(userId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }

}
