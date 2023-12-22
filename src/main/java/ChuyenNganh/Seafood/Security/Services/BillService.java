package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Repositories.IBillDetailRepository;
import ChuyenNganh.Seafood.Repositories.IBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BillService {

    private static IBillRepository billRepository;
    @Autowired
    public BillService(IBillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }
    public List<Bill> getBillByUserId(Long userId) {
        return billRepository.findBillByUser(userId);
    }

    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    public static Map<Integer, Double> thongKeTongTienTheoThang(int year) {
        List<Object[]> results = billRepository.getMonthlyRevenue(year);

        System.out.println("tKeTongTienTheoThang - Dữ liệu từ server: " + results);

        Map<Integer, Double> revenueByMonth = new HashMap<>();
        for (Object[] result : results) {
            int month = (int) result[0];
            Double total = (Double) result[1];
            revenueByMonth.put(month, total);
        }

        return revenueByMonth;
    }
    public static Map<Integer, Double> thongKeTongTienTheoNgay(int month, int year) {
        List<Object[]> results = billRepository.getDailyRevenue(month, year);

        System.out.println("tKeTongTienTheoNgay - Dữ liệu từ server: " + results);

        Map<Integer, Double> revenueByDay = new HashMap<>();
        for (Object[] result : results) {
            int day = (int) result[0];
            Double total = (Double) result[1];
            revenueByDay.put(day, total);
        }

        return revenueByDay;
    }

    public static Map<Integer, Double> thongKeTongTienTheoTuan(int year) {
        Double weeklyRevenue = billRepository.getWeeklyRevenue(year);
        Map<Integer, Double> revenueByWeek = new HashMap<>();
        revenueByWeek.put(1, weeklyRevenue); // Chú ý: 1 là số tuần hiện tại, có thể cần điều chỉnh.
        return revenueByWeek;
    }
    public static Double tongTienNam(int year) {
        return billRepository.getTotalRevenueByYear(year);
}

    @Autowired
    private IBillDetailRepository billDetailRepository;
    public List<BillDetail> getBillDetailsByBillId(Long billId) {
        return billDetailRepository.findBillDetailsByBillId(billId);
    }
}
