package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Payload.Response.MessageResponse;
import ChuyenNganh.Seafood.Repositories.ISeafoodRepository;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/seafoods")
public class APISeafoodController {

    @Autowired
    private SeafoodService seafoodService;

    @GetMapping
    public ResponseEntity<Page<Seafood>> getAllSeafoods(@RequestParam String keyword, Pageable pageable) {
        if(keyword != null){
            Page<Seafood> matchingSeafoods = seafoodService.searchSeafood(keyword, pageable);
            return ResponseEntity.ok(matchingSeafoods);
        }else {
            Page<Seafood> seafoods = seafoodService.getAllSeafoods(pageable);
            return ResponseEntity.ok(seafoods);
        }
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
}

