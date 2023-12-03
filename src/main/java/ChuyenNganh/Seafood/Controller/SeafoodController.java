package ChuyenNganh.Seafood.Controller;

import ChuyenNganh.Seafood.Entity.Seafood;
import ChuyenNganh.Seafood.Security.Services.CategoryService;
import ChuyenNganh.Seafood.Security.Services.SeafoodService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seafoods")
@RequiredArgsConstructor
public class SeafoodController {

    private final SeafoodService seafoodService;
    private final CategoryService categoryService;


    @GetMapping
    public String listSeafoods(@NotNull Model model,
                               @RequestParam(defaultValue = "0") int pageNo,
                               @RequestParam(defaultValue = "2") int pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("seafood", seafoodService.getAllSeafoods(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("totalPages", seafoodService.countSeafood() / pageSize);
        return "Seafood/list";
    }

    /*@GetMapping("/{seafoodId}")
    public String detailSeafood(@PathVariable Long seafoodId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("currentUsername", currentUsername);

        Seafood seafood = seafoodService.getSeafoodById(seafoodId);

        // Sử dụng thư viện markdown để hiển thị mô tả sản phẩm.
        String markdownDescription = markdownHtmlRenderer.render(markdownParser.parse(seafood.getDescription()));
        seafood.setDescription(markdownDescription);
        model.addAttribute("seafood", seafood);

        String description = seafood.getDescription();
        String htmlDescription = description
                .replace("{{image1}}", "<img style=\"width:100%;\" src=\"https://phuc-public-image.s3.ap-southeast-2.amazonaws.com/" + seafood.getImage_2() + "\"/>")
                .replace("{{image2}}", "<img style=\"width:100%;\" src=\"https://phuc-public-image.s3.ap-southeast-2.amazonaws.com/" + seafood.getImage_3() + "\"/>")
                .replace("{{image3}}", "<img style=\"width:100%;\" src=\"https://phuc-public-image.s3.ap-southeast-2.amazonaws.com/" + seafood.getImage_4() + "\"/>");
        model.addAttribute("description", htmlDescription);

        *//*List<Comment> comments = commentService.getCommentByProductId(productId);
        model.addAttribute("comments", comments);

        Comment newComment = new Comment();
        model.addAttribute("newComment", newComment);*//*

        return "seafood/detail";
    }*/

    @GetMapping("/add")
    public String addSeafood(Model model) {
        model.addAttribute("seafood", new Seafood());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seafood/add";
    }
    @PostMapping("/add")
    public String addSeafood(@Valid @ModelAttribute("seafood") Seafood seafood, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("categories", categoryService.getAllCategories());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "seafood/add";
        }
        seafoodService.saveSeafood(seafood);
        return "redirect:/seafoods";
    }
/*
    @GetMapping("/edit/{id}")
    public String editSeafood (@PathVariable("id") Long id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        Seafood seafood = seafoodService.getSeafoodById(id);
        if (seafood != null) {
            model.addAttribute("seafood", seafood);
            return "seafood/edit";
        }else {
            return "not-found";
        }
    }
    @PostMapping("/edit")
    public String editSeafood (@Valid @ModelAttribute("seafood") Seafood seafood, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("categories", categoryService.getAllCategories());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "seafood/edit";
        }
        seafoodService.saveSeafood(seafood);
        return "redirect:/seafoods";
    }*/

    @GetMapping("/delete/{id}")
    public String deleteSeafood (@PathVariable("id") Long id) {
        Seafood seafood = seafoodService.getSeafoodById(id);
        seafoodService.deleteSeafood(id);
        return "redirect:/seafoods";
    }

}