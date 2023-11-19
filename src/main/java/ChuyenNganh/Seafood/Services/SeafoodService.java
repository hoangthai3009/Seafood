package ChuyenNganh.Seafood.Services;

import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Repositories.ISeafoodRepository;
import lombok.RequiredArgsConstructor;
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
    public List<Seafood> getAllSeafoods(Integer pageNo, Integer pageSize, String sortBy) {
        return seafoodRepository.findAllBooks(pageNo, pageSize, sortBy);
    }
    public Seafood getSeafoodById(Long id) {
        Optional<Seafood> optional = seafoodRepository.findById(id);
        return optional.orElse(null);
    }
    public long countSeafood(){
        return seafoodRepository.count();
    }
    public void saveSeafood(Seafood seafood) {
        seafoodRepository.save(seafood);
    }
    public void deleteSeafood(Long id) {
        seafoodRepository.deleteById(id);
    }

    public List<Seafood> searchSeafood(String keyword) {
        return seafoodRepository.searchSeafood(keyword);
    }

}