package pl.lucky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lucky.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByOwnerLogin(String ownerLogin);

}
