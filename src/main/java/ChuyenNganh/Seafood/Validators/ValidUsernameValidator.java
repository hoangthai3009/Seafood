package ChuyenNganh.Seafood.Validators;

import ChuyenNganh.Seafood.Repositories.IUserRepository;
import ChuyenNganh.Seafood.Validators.Annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Autowired
    private IUserRepository userReponsitory;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userReponsitory == null) {
            return true;
        }
        return userReponsitory.findByUsername(username) == null;
    }
}
