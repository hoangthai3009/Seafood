package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Entity.Role;
import ChuyenNganh.Seafood.Repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private IRoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public void removeRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

}