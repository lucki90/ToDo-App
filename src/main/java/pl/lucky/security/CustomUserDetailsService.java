package pl.lucky.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.lucky.model.User;
import pl.lucky.model.UserRole;
import pl.lucky.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByOwnerLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        AccessFilter.setOwnerId(user.getId());
        AccessFilter.setOwnerRole(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getOwnerLogin(),
                user.getPassword(),
                convertAuthorities(user.getRole()));
    }

    private Set<GrantedAuthority> convertAuthorities(UserRole userRole) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(userRole.getRole()));

        return authorities;
    }
}
