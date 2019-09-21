package pl.lucky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lucky.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByOwnerLogin(String ownerLogin);

}
