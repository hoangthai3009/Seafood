package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.Role;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IRoleRepository;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void addRoleToUser(User user, Role role) {
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(User user, Role role) {
        user.getRoles().remove(role);
        userRepository.save(user);
    }
    public Role findRoleById(Integer roleId) {
        return roleRepository.findById(Long.valueOf(roleId)).orElse(null);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }
    public void removeRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

}