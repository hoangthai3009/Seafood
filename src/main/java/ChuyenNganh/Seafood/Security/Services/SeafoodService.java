package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Repositories.ISeafoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
public class SeafoodService {
    private final ISeafoodRepository seafoodRepository;

    public List<Seafood> getAllSeafoods() {
        return seafoodRepository.findAll();
    }
    public Page<Seafood> getAllSeafoods(Pageable pageable) {

        return seafoodRepository.findAllSeafoods(pageable);
    }

    public Page<Seafood> searchSeafood(String keyword, Pageable pageable) {
        return seafoodRepository.searchSeafood(keyword, pageable);
    }

    public Page<Seafood> getSeafoodsByCategory(Long categoryId, Pageable pageable) {
        return seafoodRepository.findByCategoryId(categoryId, pageable);
    }

    public Seafood saveSeafood(Seafood seafood) {
        return seafoodRepository.save(seafood);
    }

    public void deleteSeafood(Long id) {
        seafoodRepository.deleteById(id);
    }

    public Seafood getSeafoodById(Long seafoodId) {
        return seafoodRepository.findSeafoodById(seafoodId);
    }
}
