package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.DAOS.Cart;
import ChuyenNganh.Seafood.DTO.BillDto;
import ChuyenNganh.Seafood.Entity.Bill;
import ChuyenNganh.Seafood.Entity.BillDetail;
import ChuyenNganh.Seafood.Entity.CartItem;
import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Repositories.IBillRepository;
import ChuyenNganh.Seafood.Security.Services.CartService;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartControllerModel {
    @Autowired
    private CartService cartService;

    // Sử dụng Session
    @GetMapping
    public String showCart(HttpSession session, @NotNull Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CartItem> cartPage = cartService.getCartPage(session, pageable);
        List<CartItem> cartItems = cartPage.getContent();

        model.addAttribute("cart", cartItems);
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", cartPage.getTotalPages());
        return "Cart/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session, @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }

    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCartModel(HttpSession session, @PathVariable Long id,
                                  @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "Cart/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session, @RequestParam long seafoodId,
                            @RequestParam String seafoodName, @RequestParam double price,
                            @RequestParam String image, @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam String categoryName,
                            HttpServletRequest request) {
        var cart = cartService.getCart(session);
        String previousPage = request.getHeader("Referer");

        cart.addItems(new CartItem(seafoodId, seafoodName, price, image, categoryName, quantity));
        cartService.updateCartModel(session, cart);
        return "redirect:" + previousPage;
    }

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private SeafoodService seafoodService;
    @GetMapping("/checkout")
    public String showCheckoutForm(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));
        model.addAttribute("billDto", new BillDto());  // Tạo một đối tượng BillDto rỗng để form sử dụng
        model.addAttribute("phone");

        return "Cart/checkout";  // Tên của template Thymeleaf cho trang checkout
    }
    @PostMapping("/checkout")
    public String processCheckout(@ModelAttribute BillDto billDTO, HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");

        if (cartItems == null || cartItems.isEmpty()) {
            // Xử lý trường hợp giỏ hàng rỗng
            return "redirect:/cart-empty";
        }

        Bill bill = new Bill();
        bill.setCreatedAt(LocalDate.now());
        bill.setTotalPrice(BigDecimal.valueOf(cartService.getSumPrice(session)));
        bill.setNote(billDTO.getNote());
        billDTO.setPhone(billDTO.getPhone()); // Sử dụng trường phone từ BillDto

        Set<BillDetail> billDetails = new HashSet<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            Seafood seafood = seafoodService.getSeafoodById(item.getSeafoodId());
            if (seafood == null) {
                // Xử lý trường hợp không tìm thấy sản phẩm
                continue;
            }

            BillDetail detail = new BillDetail();
            detail.setBill(bill);
            detail.setSeafood(seafood);
            detail.setAmount((long) item.getQuantity());
            billDetails.add(detail);
        }

        bill.setBillDetails(billDetails);
        billRepository.save(bill);

        cartService.removeCart(session);

        // Xóa giỏ hàng từ session và chuyển hướng người dùng
        session.removeAttribute("cart");
        return "Cart/success";
    }

}
