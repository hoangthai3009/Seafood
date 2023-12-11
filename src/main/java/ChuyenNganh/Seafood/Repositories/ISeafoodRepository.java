package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Seafood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ISeafoodRepository extends PagingAndSortingRepository<Seafood, Long>, JpaRepository<Seafood, Long> {

    @Query("""
            SELECT s FROM Seafood s LEFT JOIN FETCH s.category
            WHERE s.name LIKE %?1%
            OR s.origin LIKE %?1%
            OR s.category.name LIKE %?1%
            """)
    Page<Seafood> searchSeafood(String keyword, Pageable pageable);

    @Query("SELECT s FROM Seafood s LEFT JOIN FETCH s.category")
    Page<Seafood> findAllSeafoods(Pageable pageable);

    @Query("SELECT s FROM Seafood s LEFT JOIN FETCH s.category WHERE s.id = :seafoodId")
    Seafood findSeafoodById(@Param("seafoodId") Long seafoodId);

    @Query("SELECT s FROM Seafood s LEFT JOIN FETCH s.category WHERE s.category.id = :categoryId")
    Page<Seafood> findSeafoodByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("""
    SELECT s FROM Seafood s LEFT JOIN FETCH s.category
    WHERE (s.name LIKE %:keyword%
            OR s.origin LIKE %:keyword%
            OR s.category.name LIKE %:keyword%)
            AND s.category.id = :categoryId
    """)
    Page<Seafood> findSeafoodByCategoryIdAndSearch(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}