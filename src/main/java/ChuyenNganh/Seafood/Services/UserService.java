package ChuyenNganh.Seafood.Services;

import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IRoleRepository;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public void addRoleToUser(Long userId, Long roleId) {
        userRepository.addRoleToUser(userId, roleId);
    }

    public void removeRoleFromUser(Long userId, Long roleId) {
        userRepository.removeRoleFromUser(userId, roleId);
    }

    public String[] getRolesOfUser(Long id) {
        return userRepository.getRolesOfUser(id);
    }

    public String[] getRolesOfRole(String username) {
        return userRepository.getRolesOfUser(username);
    }

    public String saveUser(User user) {
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByRoleName("USER");
        if (roleId != 0 && userId != 0) {
            userRepository.addRoleToUser(userId, roleId);
        }
        return "User registered Successfully";
    }

}
