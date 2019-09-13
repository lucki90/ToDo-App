package pl.lucky.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/loginForm").permitAll()
                .antMatchers("/registrationForm").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/loginForm").permitAll()
                .and()
                .logout().logoutUrl("/logoutForm").logoutSuccessUrl("/loginForm").permitAll();
    }

}
