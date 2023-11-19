package ChuyenNganh.Seafood.Validators;

import ChuyenNganh.Seafood.Entity.Category;
import ChuyenNganh.Seafood.Validators.Annotation.ValidCategoryId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class ValidCategoryIdValidator implements ConstraintValidator<ValidCategoryId, Category> {
    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return category != null && category.getId() != null;
    }
}