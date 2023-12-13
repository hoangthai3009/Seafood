package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.DTO.BillDto;
import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Mapper.BillMapper;
import ChuyenNganh.Seafood.Security.Services.CategoryService;
import ChuyenNganh.Seafood.Security.Services.IImageService;
import ChuyenNganh.Seafood.Security.Services.RoleService;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import ChuyenNganh.Seafood.Utils.FileUploadUlti;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ChuyenNganh.Seafood.Security.Services.BillService BillService;
    @Autowired
    private BillMapper BillMapper;
    @Autowired
    IImageService imageService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SeafoodService seafoodService;
    @Autowired
    private CategoryService categoryService;



    @GetMapping
    public String index() {
        return "Admin/index";
    }

    @GetMapping("/seafoods")
    public String getAllSeafoodsPage(Model model) {
        List<Seafood> seafoods = seafoodService.getAllSeafoods();
        model.addAttribute("seafoods", seafoods);
        return "Admin/seafood/list-seafood";
    }

    @GetMapping("/add-seafood")
    public String addSeafood(Model model) {
        model.addAttribute("seafood", new Seafood());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Admin/seafood/add-seafood";
    }
    @PostMapping("/add-seafood")
    public String saveSeafood(@ModelAttribute(name = "seafood") Seafood seafood,
                              RedirectAttributes ra,
                              @RequestParam("mainImage") MultipartFile mainMultipartFile,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraMultipartFiles
                              ) throws IOException {
        String mainImage = imageService.save(mainMultipartFile);
        seafood.setMainImage(mainImage);

        int count = 0;
        for(MultipartFile extraMultipart : extraMultipartFiles){
            String extraImage = imageService.save(extraMultipart);
            if(count == 0) seafood.setExtraImage1(extraImage);
            if(count == 1) seafood.setExtraImage2(extraImage);
            if(count == 2) seafood.setExtraImage3(extraImage);
            count ++;
        }

        Seafood saveSeafood = seafoodService.saveSeafood(seafood);

        ra.addFlashAttribute("message","Seafood save successfully.");
        return "redirect:/admin/seafoods";
    }
    @GetMapping("/edit-seafood/{id}")
    public String editSeafood (@PathVariable("id") Long id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        Seafood seafood = seafoodService.getSeafoodById(id);
        if (seafood != null) {
            model.addAttribute("seafood", seafood);
            return "Admin/seafood/edit-seafood";
        }else {
            return "not-found";
        }
    }
    @PostMapping("/edit-seafood")
    public String updateSeafood(@ModelAttribute(name = "seafood") Seafood seafood,
                                RedirectAttributes ra,
                                @RequestParam(value = "mainImage", required = false) MultipartFile mainMultipartFile,
                                @RequestParam(value = "extraImage", required = false) MultipartFile[] extraMultipartFiles,
                                boolean exist) throws IOException {
        // Kiểm tra xem có hình chính mới không
        if (!mainMultipartFile.isEmpty()) {
            String mainImage = imageService.save(mainMultipartFile);
            seafood.setMainImage(mainImage);
        }else {
            // Giữ nguyên giá trị ảnh chính hiện tại nếu không có cập nhật
            Seafood currentSeafood = seafoodService.getSeafoodById(seafood.getId());
            seafood.setMainImage(currentSeafood.getMainImage());
        }

        // Kiểm tra và cập nhật hình phụ
        int count = 0;
        for (MultipartFile extraMultipart : extraMultipartFiles) {
            if (!extraMultipart.isEmpty()) {
                String extraImage = imageService.save(extraMultipart);
                // Cập nhật giá trị ảnh phụ tương ứng
                if (count == 0) seafood.setExtraImage1(extraImage);
                else if (count == 1) seafood.setExtraImage2(extraImage);
                else if (count == 2) seafood.setExtraImage3(extraImage);
                count++;
            }
            else{
                // Giữ nguyên giá trị ảnh phụ hiện tại nếu không có cập nhật
                Seafood currentSeafood = seafoodService.getSeafoodById(seafood.getId());
                if (count == 0) seafood.setExtraImage1(currentSeafood.getExtraImage1());
                else if (count == 1) seafood.setExtraImage2(currentSeafood.getExtraImage2());
                else if (count == 2) seafood.setExtraImage3(currentSeafood.getExtraImage3());
                count++;
            }
        }
        // Lưu thông tin seafood cập nhật
        seafoodService.saveSeafood(seafood);
        ra.addFlashAttribute("message", "Seafood edited successfully.");
        return "redirect:/admin/seafoods";
    }



    @GetMapping("/delete/{id}")
    public String deleteSeafood (@PathVariable("id") Long id) {
        Seafood seafood = seafoodService.getSeafoodById(id);
        seafoodService.deleteSeafood(id);
        return "redirect:/admin/seafoods";
    }

    @GetMapping("/bills")
    public String getAllBills(Model model) {
        List<Bill> bills = BillService.getAllBill();
        List<BillDto> billDtos = bills.stream()
                .map(BillMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("bills", billDtos);
        return "Admin/bill/list-bill";
    }

    // Thống kê theo tháng
    @GetMapping("/thongKeTheoThang-data")
    @ResponseBody
    public Map<Integer, BigDecimal> thongKeData(@RequestParam("year") int year) {
        return BillService.thongKeTongTienTheoThang(year);
    }

    // Thống kê theo ngày
    @GetMapping("/thongKeTheoNgay")
    public String thongKeNgay(Model model) {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        Map<Integer, BigDecimal> revenueByDay = BillService.thongKeTongTienTheoNgay(month, year);
        model.addAttribute("revenueByDay", revenueByDay);
        return "Admin/bill/thongKeTheoNgay";
    }
    @GetMapping("/thongKeTheoNgay-data")
    @ResponseBody
    public Map<Integer, BigDecimal> thongKeNgayData(@RequestParam("month") int month,
                                                    @RequestParam("year") int year) {
        return BillService.thongKeTongTienTheoNgay(month, year);
    }
    @GetMapping("/thongKeTheoTuan-data")
    @ResponseBody
    public Map<Integer, BigDecimal> thongKeTuanData(@RequestParam("year") int year){
        return BillService.thongKeTongTienTheoTuan(year);
    }
    @GetMapping("/thongKeTheoNam-data")
    @ResponseBody
    public BigDecimal thongKeNamData() {
        int currentYear = LocalDate.now().getYear();
        return BillService.tongTienNam(currentYear);
    }
/*
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list-user")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Admin/list-user";
    }
    //endregion

    //region Role
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/roles")
    public String getAllRole(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "Admin/role/list-role";
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add-role")
    public String addRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "Admin/role/add-role";
    }
    @PostMapping("/add-role")
    public String addRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.error("Lỗi binding trường {}: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "Admin/role/add-role";
        }
        logger.info("Tạo thành công Quyền có Id{}", role.getRoleId());
        roleService.saveRole(role);
        return "redirect:/admin/roles";
    }

*//*    @GetMapping("/edit-role/{roleId}")
    public String editRoleForm(@PathVariable("roleId") UUID roleId, Model model) {
        Role role = roleService.getRoleById(roleId);
        model.addAttribute("role", role);
        return "Admin/role/edit-role";
    }*//*
    @PostMapping("/edit-role/{roleId}")
    public String editRole(@Valid @ModelAttribute("role") Role role,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.error("Lỗi binding trường {}: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "Admin/role/edit-role";
        }
        roleService.saveRole(role);
        logger.info("Sửa thành công role có Id {}", role.getRoleId());
        return "redirect:/admin/roles";
    }*/
}
