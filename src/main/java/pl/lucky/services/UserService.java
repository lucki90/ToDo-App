package pl.lucky.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lucky.model.User;
import pl.lucky.model.UserRole;
import pl.lucky.repository.UserRepository;
import pl.lucky.repository.UserRoleRepository;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class UserService  {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private UserRepository userRepository;
    private UserRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        super();
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(UserRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addWithDefaultRole(User user) throws SQLIntegrityConstraintViolationException{
        UserRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
        user.setRole(defaultRole);
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new SQLIntegrityConstraintViolationException(e);
        }
    }
}
