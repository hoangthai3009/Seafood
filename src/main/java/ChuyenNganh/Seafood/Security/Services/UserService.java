package ChuyenNganh.Seafood.Security.Services;
import ChuyenNganh.Seafood.Entity.Status;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    // Lưu người dùng
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public String[] getRolesOfUser(Long id) {
        return userRepository.getRolesOfUser(id);
    }
    public void addRoleToUser(Long userId, Long roleId) {
        userRepository.addRoleToUser(userId, roleId);
    }
    public void removeRoleFromUser(Long userId, Long roleId) {
        userRepository.removeRoleFromUser(userId, roleId);
    }
    public void saveUserStatus(User user) {
        user.setStatus(Status.ONLINE);
        userRepository.save(user);
    }
    public void disconnect(User user) {
        var storedUser = userRepository.findById(user.getId()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
