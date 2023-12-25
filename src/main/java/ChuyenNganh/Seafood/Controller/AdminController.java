package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.*;
import ChuyenNganh.Seafood.Security.Services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    IImageService imageService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SeafoodService seafoodService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BillService billService;
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
        seafoodService.saveSeafood(seafood);
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
            return "Error/404";
        }
    }
    @PostMapping("/edit-seafood")
    public String updateSeafood(@ModelAttribute(name = "seafood") Seafood seafood,
                                RedirectAttributes ra,
                                @RequestParam(value = "mainImage", required = false) MultipartFile mainMultipartFile,
                                @RequestParam(value = "extraImage", required = false) MultipartFile[] extraMultipartFiles) throws IOException {
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
            }
            else{
                // Giữ nguyên giá trị ảnh phụ hiện tại nếu không có cập nhật
                Seafood currentSeafood = seafoodService.getSeafoodById(seafood.getId());
                if (count == 0) seafood.setExtraImage1(currentSeafood.getExtraImage1());
                else if (count == 1) seafood.setExtraImage2(currentSeafood.getExtraImage2());
                else if (count == 2) seafood.setExtraImage3(currentSeafood.getExtraImage3());
            }
            count++;
        }
        // Lưu thông tin seafood cập nhật
        seafoodService.saveSeafood(seafood);
        ra.addFlashAttribute("message", "Seafood edited successfully.");
        return "redirect:/admin/seafoods";
    }



    @GetMapping("/delete/{id}")
    public String deleteSeafood (@PathVariable("id") Long id) {
        seafoodService.deleteSeafood(id);
        return "redirect:/admin/seafoods";
    }
    @Autowired
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    @GetMapping("/assign-role/{id}")
    public String addRoleToUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getAllRoles();
        String[] rolesOfUser = userService.getRolesOfUser(id);

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("roleOfUser", rolesOfUser);
        return "Admin/role/assign-role";

    }
    @PostMapping("/assign-role")
    public String addRoleToUser(@RequestParam Long userId,
                                @RequestParam Long roleId, RedirectAttributes redirectAttributes) {
        String[] roles = userService.getRolesOfUser(userId);
        String roleName = String.valueOf(roleService.getRoleById(roleId).getName());

        if (Arrays.asList(roles).contains(roleName)) {
            redirectAttributes.addFlashAttribute("exists", "Quyền đã tồn tại cho người dùng này");
            logger.warn("Quyền có Id {} đã tồn tại cho người dùng có Id {}", roleService.getRoleById(roleId).getId(), userId);
        } else {
            userService.addRoleToUser(userId, roleId);
            redirectAttributes.addFlashAttribute("success", "Đã thêm quyền cho người dùng này");
            logger.info("Gán thành công quyền có Id {} cho user có Id {}", roleService.getRoleById(roleId).getId(), userId);
        }
        return "redirect:/admin/assign-role/" + userId;
    }

    @PostMapping("/remove-role-from-user")
    public String removeRoleFromUser(@RequestParam("userId") Long userId,
                                     @RequestParam("roleId") Long roleId, RedirectAttributes redirectAttributes) {
        // Lấy danh sách các quyền của người dùng
        String[] roles = userService.getRolesOfUser(userId);
        // Lấy tên quyền dựa trên roleId
        String roleName = String.valueOf(roleService.getRoleById(roleId).getDescription());

        if (Arrays.asList(roles).contains(roleName)) {
            userService.removeRoleFromUser(userId, roleId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa quyền cho người dùng này");
            logger.info("Xóa thành công quyền có Id {} cho user có Id {}", roleService.getRoleById(roleId).getId(), userId);
        } else {
            // Người dùng không có quyền này
            redirectAttributes.addFlashAttribute("notExist", "Người dùng không có quyền này");
            logger.warn("Người dùng có Id {} không có quyền có Id {} để xóa", userId, roleService.getRoleById(roleId).getId());
        }

        // Chuyển hướng trở lại trang gán quyền cho người dùng
        return "redirect:/admin/assign-role/" + userId;
    }

    @GetMapping("/list-user")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Admin/list-user";
    }

    @GetMapping("/roles")
    public String getAllRole(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "Admin/role/list-role";
    }
// Thêm role
//    @PreAuthorize("hasAuthority('ADMIN')")
    /*@GetMapping("/add-role")
    public String addRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "Admin/role/add-role";
    }

    @PostMapping("/add-role")
    public String addRole(
            @RequestParam String roleName, // Change the parameter to a String
            @Valid @ModelAttribute("role") Role role,
            BindingResult bindingResult,
            Model model
    ) {
        Role newRole = new Role();
        try {
            ERole roleEnum = ERole.valueOf(roleName.toUpperCase());
            newRole.setName(roleEnum);
        } catch (IllegalArgumentException e) {
            model.addAttribute("roleName_error", "Tên vai trò không hợp lệ.");
            model.addAttribute("role", newRole);
            return "Admin/role/add-role";
        }

        newRole.setDescription(role.getDescription());
        roleService.saveRole(newRole);

        return "redirect:/admin/roles";
    }

    @GetMapping("/edit-role/{roleId}")
    public String editRoleForm(@PathVariable("roleId") Long roleId, Model model) {
        Role role = roleService.getRoleById(roleId);
        model.addAttribute("role", role);
        return "Admin/role/edit-role";
    }
    @PostMapping("/edit-role/{roleId}")
    public String editRole(@Valid @ModelAttribute("role") Role role,
                           BindingResult bindingResult, Model model, @PathVariable String roleId) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "Admin/role/edit-role";
        }
        roleService.saveRole(role);
        return "redirect:/admin/roles";
    }
    @GetMapping("/delete-role/{roleId}")
    public String deleteRole(@PathVariable("roleId") Long roleId) {
        Role role = roleService.getRoleById(roleId);
        roleService.removeRole(roleId);
        logger.info("Xóa thành công role {}", role.getName());
        return "redirect:/admin/roles";
    }
    */
    @GetMapping("/bills")
    public String getAllBills(Model model) {
        List<Bill> bills = billService.getAllBills();
        model.addAttribute("bills", bills);
        return "Admin/bill/list-bill";
    }
    @GetMapping("/edit-bill/{billId}")
    public String showEditBillForm(@PathVariable Long billId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Bill> bill = billService.getBillById(billId);
        if (bill.isPresent()) {
            SimpleDateFormat isoDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = isoDateFormatter.format(bill.get().getCreatedAt());

            model.addAttribute("bill", bill.get());
            model.addAttribute("formattedCreatedAt", formattedDate);

            return "Admin/bill/edit-bill";
        } else {
            redirectAttributes.addFlashAttribute("error", "Bill không tồn tại.");
            return "redirect:/admin/bills";
        }
    }
    @PostMapping("/edit-bill")
    public String editBill(@RequestParam Long billId, @ModelAttribute Bill bill, RedirectAttributes redirectAttributes) {
        Optional<Bill> existingBillOpt = billService.getBillById(billId);
        if (existingBillOpt.isPresent()) {
            Bill existingBill = existingBillOpt.get();
            existingBill.setTotalPrice(bill.getTotalPrice());
            existingBill.setNote(bill.getNote());
            existingBill.setAddress(bill.getAddress());
            existingBill.setUser(bill.getUser());
            billService.saveBill(existingBill);
            redirectAttributes.addFlashAttribute("success", "Bill đã được cập nhật thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Bill không tồn tại.");
        }
        return "redirect:/admin/bills";
    }

    // Phương thức xóa bill
    @GetMapping("/delete-bill/{billId}")
    public String deleteBill(@PathVariable Long billId, RedirectAttributes redirectAttributes) {
        Optional<Bill> bill = billService.getBillById(billId);
        if (bill.isPresent()) {
            Set<BillDetail> billDetails = bill.get().getBillDetails();
            for (BillDetail billDetail : billDetails) {
                bill.get().removeBillDetail(billDetail);
            }
            billService.deleteBill(billId);
            redirectAttributes.addFlashAttribute("success", "Bill đã được xóa thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Bill không tồn tại.");
        }
        return "redirect:/admin/bills";

    }
    @GetMapping("/bills/{billId}/details")
    public String getBillDetailsPage(@PathVariable Long billId, Model model) {
        List<BillDetail> billDetails = billService.getBillDetailsByBillId(billId);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("billId", billId);
        return "Admin/bill/bill-detail";
    }

    @GetMapping("/chat")
    public String chat(){
        return "Admin/chat";
    }
}
