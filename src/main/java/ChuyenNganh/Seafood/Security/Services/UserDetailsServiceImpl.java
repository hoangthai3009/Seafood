package ChuyenNganh.Seafood.Security.Services;

import ChuyenNganh.Seafood.Constants.Provider;
import ChuyenNganh.Seafood.Entity.ERole;
import ChuyenNganh.Seafood.Entity.Role;
import ChuyenNganh.Seafood.Entity.User;
import ChuyenNganh.Seafood.Repositories.IRoleRepository;
import ChuyenNganh.Seafood.Repositories.IUserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username or email: " + usernameOrEmail));

        return UserDetailsImpl.build(user);
    }

    public void saveOauthUser(String email, @NotNull String username) throws RoleNotFoundException {
        if(userRepository.findByUsername(username).isPresent())
            return;
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);
        Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Role is not found."));
        user.getRoles().add(role);
        userRepository.save(user);
    }

}