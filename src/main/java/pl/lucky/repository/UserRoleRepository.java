package pl.lucky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lucky.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByRole(String role);
}
