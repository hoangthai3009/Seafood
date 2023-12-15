package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Comment;
import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Security.Services.CommentService;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/seafoods")
public class APISeafoodController {

    @Autowired
    private SeafoodService seafoodService;
    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<Seafood>> getAllSeafoods(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "null") String sort,
            Pageable pageable) {

        Page<Seafood> seafoods;

        if (keyword != null) {
            seafoods = seafoodService.searchSeafood(keyword, pageable);
        } else {
            // Nếu không có từ khóa, thực hiện lấy tất cả và sắp xếp theo giá
            Sort sorting;
            if ("giaTang".equals(sort)) {
                sorting = Sort.by("price").ascending();
            } else if ("giaGiam".equals(sort)) {
                sorting = Sort.by("price").descending();
            } else {
                sorting = Sort.unsorted();
            }

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
            seafoods = seafoodService.getAllSeafoods(pageable);
        }

        return ResponseEntity.ok(seafoods);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Seafood> getSeafood(@PathVariable Long id) {
        Seafood seafood = seafoodService.getSeafoodById(id);
        if (seafood != null) {
            return ResponseEntity.ok(seafood);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category")
    public ResponseEntity<Page<Seafood>> getSeafoodsByCategory(@RequestParam Long categoryId, @RequestParam String keyword, Pageable pageable) {
        if(keyword != null){
            Page<Seafood> seafoodsByCategoryAndKeyword = seafoodService.getSeafoodsByCategoryAndSearch(categoryId, keyword, pageable);
            return ResponseEntity.ok(seafoodsByCategoryAndKeyword);
        }else {
            Page<Seafood> seafoodsByCategory = seafoodService.getSeafoodsByCategory(categoryId, pageable);
            return ResponseEntity.ok(seafoodsByCategory);
        }
    }

    @GetMapping("/{seafoodId}/comments")
    public ResponseEntity<List<Comment>> getCommentsBySeafood(@PathVariable Long seafoodId) {
        List<Comment> comments = commentService.getCommentBySeafoodId(seafoodId);
        return ResponseEntity.ok(comments);
    }
    @PostMapping("/{seafoodId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long seafoodId, @RequestBody Comment comment) {
        Comment newComment = commentService.addComment(seafoodId, comment);
        if (newComment != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        Comment updatedComment = commentService.updateComment(commentId, comment);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok().build(); // Trả về phản hồi thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Trả về lỗi server nếu có vấn đề xảy ra
        }
    }

}

