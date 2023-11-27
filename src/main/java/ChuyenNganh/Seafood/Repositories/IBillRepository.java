package ChuyenNganh.Seafood.Repositories;

import ChuyenNganh.Seafood.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT b FROM Bill b, User u " +
            "WHERE b.user.userId = :userId")
    Bill findBillByUser(@Param("userId") UUID userId);

    @Query("SELECT MONTH(b.createdAt) AS month, SUM(b.totalPrice) AS total " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdAt) = :year " +
            "GROUP BY MONTH(b.createdAt)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    @Query("SELECT DAY(b.createdAt) AS day, SUM(b.totalPrice) AS total " +
            "FROM Bill b " +
            "WHERE MONTH(b.createdAt) = :month AND YEAR(b.createdAt) = :year " +
            "GROUP BY DAY(b.createdAt)")
    List<Object[]> getDailyRevenue(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdAt) = :year AND WEEK(b.createdAt) = WEEK(NOW())")
    BigDecimal getWeeklyRevenue(@Param("year") int year);

    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdAt) = :year")
    BigDecimal getTotalRevenueByYear(@Param("year") int year);



}
