package pl.lucky.data;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lucky.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
