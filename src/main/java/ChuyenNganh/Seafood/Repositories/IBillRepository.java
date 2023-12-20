package ChuyenNganh.Seafood.Repositories;
import org.springframework.data.jpa.repository.Query;
import ChuyenNganh.Seafood.Entity.Bill;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT b FROM Bill b WHERE b.user.id = :userId")
    List<Bill> findBillByUser(@Param("userId") Long userId);

    @Query("SELECT b " +
            "FROM Bill b " +
            "WHERE b.id = :keyword OR " +
            "b.address = :keyword OR " +
            "b.note = :keyword")
    List<Bill> searchOrders(@Param("keyword") String keyword);


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
    Double getWeeklyRevenue(@Param("year") int year);

    @Query("SELECT SUM(b.totalPrice) " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdAt) = :year")
    Double getTotalRevenueByYear(@Param("year") int year);


}
