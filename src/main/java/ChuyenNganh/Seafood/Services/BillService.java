package ChuyenNganh.Seafood.Services;

import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BillService {
    @Autowired
    private IBillRepository billRepository;

    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    public Bill getBillByUserId(UUID userId) {
        return billRepository.findBillByUser(userId);
    }

    public List<Bill> getAllBill() {
        return billRepository.findAll();
    }

    public void createBill(Bill bill, User user) {
        bill.setUser(user);
        bill.setTotalPrice(BigDecimal.valueOf(0));
        bill.setCreatedAt(LocalDate.now());
        billRepository.save(bill);
    }

    public void updateBill(Bill bill) {
        billRepository.save(bill);
    }

    public Map<Integer, BigDecimal> thongKeTongTienTheoThang(int year) {
        List<Object[]> results = billRepository.getMonthlyRevenue(year);

        System.out.println("tKeTongTienTheoThang - Dữ liệu từ server: " + results);

        Map<Integer, BigDecimal> revenueByMonth = new HashMap<>();
        for (Object[] result : results) {
            int month = (int) result[0];
            BigDecimal total = (BigDecimal) result[1];
            revenueByMonth.put(month, total);
        }

        return revenueByMonth;
    }

    public Map<Integer, BigDecimal> thongKeTongTienTheoNgay(int month, int year) {
        List<Object[]> results = billRepository.getDailyRevenue(month, year);

        System.out.println("tKeTongTienTheoNgay - Dữ liệu từ server: " + results);

        Map<Integer, BigDecimal> revenueByDay = new HashMap<>();
        for (Object[] result : results) {
            int day = (int) result[0];
            BigDecimal total = (BigDecimal) result[1];
            revenueByDay.put(day, total);
        }

        return revenueByDay;
    }

    public Map<Integer, BigDecimal> thongKeTongTienTheoTuan(int year) {
        BigDecimal weeklyRevenue = billRepository.getWeeklyRevenue(year);
        Map<Integer, BigDecimal> revenueByWeek = new HashMap<>();
        revenueByWeek.put(1, weeklyRevenue); // Chú ý: 1 là số tuần hiện tại, có thể cần điều chỉnh.
        return revenueByWeek;
    }
    public BigDecimal tongTienNam(int year) {
        return billRepository.getTotalRevenueByYear(year);
    }
}

