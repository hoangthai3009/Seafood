package ChuyenNganh.Seafood.Services;

import ChuyenNganh.Seafood.Entity.Category;
import ChuyenNganh.Seafood.Repositories.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Category getCategoryById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        } else {
            throw new RuntimeException("Không tìm thấy Danh mục này");
        }
    }
    public Category saveCategory (Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory (Long id) {
        categoryRepository.deleteById(id);
    }
}
