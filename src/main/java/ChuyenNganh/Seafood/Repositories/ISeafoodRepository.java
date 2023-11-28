package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Seafood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ISeafoodRepository extends PagingAndSortingRepository<Seafood, Long>, JpaRepository<Seafood, Long> {
    default List<Seafood> findAllSeafoods(Integer pageNo, Integer pageSize, String sortBy) {
        return findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy))).getContent();
    }
    @Query("""
            SELECT s FROM Seafood s
            WHERE s.name LIKE %?1%
            OR s.origin LIKE %?1%
            OR s.category.name LIKE %?1%
            """)
    Page<Seafood> searchSeafood(String keyword, Pageable pageable);

    Page<Seafood> findByCategoryId(Long categoryId, Pageable pageable);
}