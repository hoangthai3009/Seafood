/*
package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IRoleRepository;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Autowired
    public UserService(IUserRepository userRepository, IRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

    }

    // Đăng ký người dùng mới
    public User registerNewUser(String username, String email, String password, String fullname, String phone) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setEnabled(true); // Hoặc false nếu muốn yêu cầu xác nhận từ người dùng

        // Nếu bạn có nhiều quyền, bạn có thể thêm logic để lấy danh sách quyền từ frontend và thêm vào người dùng
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        roles.add(defaultRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    // Tìm người dùng bằng email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Tìm người dùng bằng tên đăng nhập
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}*/
