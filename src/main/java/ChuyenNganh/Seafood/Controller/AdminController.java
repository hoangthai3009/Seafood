package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.DTO.BillDto;
import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.Role;
import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Mapper.BillMapper;
import ChuyenNganh.Seafood.Services.*;
import ChuyenNganh.Seafood.Utils.FileUploadUlti;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private BillService BillService;
    @Autowired
    private BillMapper BillMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    private final SeafoodService seafoodService;
    private final CategoryService categoryService;
    Logger logger = LoggerFactory.getLogger(AdminController.class);


    public AdminController(CategoryService categoryService, SeafoodService seafoodService) {
        this.categoryService = categoryService;
        this.seafoodService = seafoodService;
    }

    @GetMapping("/seafoods")
    public String listSeafoods(@NotNull Model model,
                               @RequestParam(defaultValue = "0") int pageNo,
                               @RequestParam(defaultValue = "2") int pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("seafood", seafoodService.getAllSeafoods(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("totalPages", seafoodService.countSeafood() / pageSize);
        return "Admin/seafood/list-seafood";
    }

    @GetMapping
    public String index() {
        return "Admin/index";
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
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraMultipartFiles) throws IOException {
        String mainImageName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
        seafood.setMainImage(mainImageName);

        int count = 0;
        for(MultipartFile extraMultipart : extraMultipartFiles){
            String extraImageName = StringUtils.cleanPath(extraMultipart.getOriginalFilename());
            if(count == 0) seafood.setExtraImage1(extraImageName);
            if(count == 1) seafood.setExtraImage2(extraImageName);
            if(count == 2) seafood.setExtraImage3(extraImageName);
            count ++;
        }

        Seafood saveSeafood = seafoodService.saveSeafood(seafood);
        String uploadDir = "src/main/resources/static/img/seafoods/" + saveSeafood.getId();
        FileUploadUlti.saveFile(uploadDir,mainMultipartFile,mainImageName);

        for(MultipartFile extraMultipart : extraMultipartFiles){
            String fileName = StringUtils.cleanPath(extraMultipart.getOriginalFilename());
            FileUploadUlti.saveFile(uploadDir,extraMultipart,fileName);
        }

        ra.addFlashAttribute("message","Seafood save successfully.");
        return "redirect:/Admin/seafoods";
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
    @PostMapping("/edit-seafood/{id}")
    public String updateSeafood(@PathVariable Long id,
                                @ModelAttribute(name = "seafood") Seafood seafood,
                                RedirectAttributes ra,
                                @RequestParam(value = "mainImage", required = false) MultipartFile mainMultipartFile,
                                @RequestParam(value = "extraImage",required = false) MultipartFile[] extraMultipartFiles) throws IOException {
        Seafood existingSeafood = seafoodService.getSeafoodById(id);

        // Check if seafood exists
        if (existingSeafood == null) {
            // Handle the case where the seafood with the given id is not found
            // You can redirect to an error page or handle it as appropriate
            return "redirect:/error";
        }

        existingSeafood.setName(seafood.getName());
        // Set other attributes you want to update

        String mainImageName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
        existingSeafood.setMainImage(mainImageName);

        int count = 0;
        for (MultipartFile extraMultipart : extraMultipartFiles) {
            String extraImageName = StringUtils.cleanPath(extraMultipart.getOriginalFilename());
            if (count == 0) existingSeafood.setExtraImage1(extraImageName);
            if (count == 1) existingSeafood.setExtraImage2(extraImageName);
            if (count == 2) existingSeafood.setExtraImage3(extraImageName);
            count++;
        }

        Seafood updatedSeafood = seafoodService.saveSeafood(existingSeafood);
        String uploadDir = "src/main/resources/static/img/seafoods/" + updatedSeafood.getId();
        FileUploadUlti.saveFile(uploadDir, mainMultipartFile, mainImageName);

        for (MultipartFile extraMultipart : extraMultipartFiles) {
            String fileName = StringUtils.cleanPath(extraMultipart.getOriginalFilename());
            FileUploadUlti.saveFile(uploadDir, extraMultipart, fileName);
        }

        ra.addFlashAttribute("message", "Seafood updated successfully.");
        return "redirect:/Admin/seafoods";
    }
    /*@PostMapping("/edit-seafood")
    public String editSeafood(@ModelAttribute(name = "seafood") Seafood seafood,
                             RedirectAttributes ra,
                             @RequestParam("fileImage")MultipartFile multipartFile
    ) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        seafood.setImage(fileName);
        Seafood saveSeafood = seafoodService.saveSeafood(seafood);

        String uploadDir = "src/main/resources/static/img/seafoods/";
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try(InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw new IOException("Không thể lưu file tải lên: " + fileName);
        }
        ra.addFlashAttribute("message","Ảnh được lưu thành công.");

        return "redirect:/Admin/seafoods";
    }
*/
    @GetMapping("/delete/{id}")
    public String deleteSeafood (@PathVariable("id") Long id) {
        Seafood seafood = seafoodService.getSeafoodById(id);
        seafoodService.deleteSeafood(id);
        return "redirect:/Admin/seafoods";
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
    @GetMapping("/thongKeTheoThang")
    public String thongKeTheoThang(Model model) {
        int year = LocalDate.now().getYear();
        Map<Integer, BigDecimal> revenueByMonth = BillService.thongKeTongTienTheoThang(year);
        model.addAttribute("revenueByMonth", revenueByMonth);
        return "Admin/bill/thongKeTheoThang";
    }
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
        return "redirect:/Admin/roles";
    }

/*    @GetMapping("/edit-role/{roleId}")
    public String editRoleForm(@PathVariable("roleId") UUID roleId, Model model) {
        Role role = roleService.getRoleById(roleId);
        model.addAttribute("role", role);
        return "Admin/role/edit-role";
    }*/
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
        return "redirect:/Admin/roles";
    }
}
