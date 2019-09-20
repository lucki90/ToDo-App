package pl.lucky.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth.jdbcAuthentication();
        builder.dataSource(dataSource);
        JdbcUserDetailsManager userDetailsService = builder.getUserDetailsService();
        //Zmiana domyślnej kofnfiguracji tabeli users na accountes
//        userDetailsService.setUsersByUsernameQuery("select username,password,enabled from accounts where username = ?");
//        userDetailsService.setCreateUserSql("insert into accounts (username, password, enabled) values (?,?,?)");
        //Zmiana domyślnej konfiguracji tabeli authorities na roles
        userDetailsService.setAuthoritiesByUsernameQuery("select username, role_name from roles where username = ?");
        userDetailsService.setCreateAuthoritySql("insert into roles (username, role_name) values (?,?)");
    }

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
