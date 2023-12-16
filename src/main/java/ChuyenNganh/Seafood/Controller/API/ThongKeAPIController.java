package ChuyenNganh.Seafood.Controller.API;

import ChuyenNganh.Seafood.Security.Services.BillService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class ThongKeAPIController {
    // Thống kê theo tháng
    @GetMapping("/thongKeTheoThang-data")
    @ResponseBody
    public Map<Integer, Double> thongKeData(@RequestParam("year") int year) {
        return BillService.thongKeTongTienTheoThang(year);
    }

    // Thống kê theo ngày
    @GetMapping("/thongKeTheoNgay-data")
    @ResponseBody
    public Map<Integer, Double> thongKeNgayData(@RequestParam("month") int month,
                                                    @RequestParam("year") int year) {
        return BillService.thongKeTongTienTheoNgay(month, year);
    }


    @GetMapping("/thongKeTheoTuan-data")
    @ResponseBody
    public Map<Integer, Double> thongKeTuanData(@RequestParam("year") int year){
        return BillService.thongKeTongTienTheoTuan(year);
    }
    @GetMapping("/thongKeTheoNam-data")
    @ResponseBody
    public Double thongKeNamData() {
        int currentYear = LocalDate.now().getYear();
        return BillService.tongTienNam(currentYear);
    }

}
