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

@RestController
@RequestMapping("/api/seafoods")
public class APISeafoodController {

    @Autowired
    private SeafoodService seafoodService;

    @GetMapping
    public ResponseEntity<Page<Seafood>> getAllSeafoods(Pageable pageable) {
        Page<Seafood> seafoods = seafoodService.getAllSeafoods(pageable);
        return ResponseEntity.ok(seafoods);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Seafood>> searchSeafoods(@RequestParam String keyword, Pageable pageable) {
        Page<Seafood> matchingSeafoods = seafoodService.searchSeafood(keyword, pageable);
        return ResponseEntity.ok(matchingSeafoods);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<Seafood>> getSeafoodsByCategory(@RequestParam Long categoryId, Pageable pageable) {
        Page<Seafood> seafoodsByCategory = seafoodService.getSeafoodsByCategory(categoryId, pageable);
        return ResponseEntity.ok(seafoodsByCategory);
    }
}

