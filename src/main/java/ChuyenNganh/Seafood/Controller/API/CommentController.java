package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Comment;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Payload.Request.CheckoutRequest;
import ChuyenNganh.Seafood.Payload.Request.CommentRequest;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import ChuyenNganh.Seafood.Security.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comment/{seafoodId}")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsBySeafoodId(@PathVariable Long seafoodId) {
        List<Comment> comments = commentService.getCommentBySeafoodId(seafoodId);
        return ResponseEntity.ok(comments);
    }

    @Autowired
    IUserRepository userRepository ;
    @PostMapping("/add")
    public ResponseEntity<Comment> addCommentToSeafood(@PathVariable Long seafoodId,
                                                       @RequestBody CommentRequest commentRequest) throws Exception {
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new Exception("User not found"));
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setCreatedAt(LocalDate.now());
        comment.setUser(user);
        System.out.println("Received userId: " + commentRequest.getUserId());

        Comment createdComment = commentService.addComment(seafoodId, comment);
        if (createdComment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/edit/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedComment) {
        Comment updated = commentService.updateComment(commentId, updatedComment);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build(); // Hoặc xử lý lỗi khác tùy theo yêu cầu của bạn
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

