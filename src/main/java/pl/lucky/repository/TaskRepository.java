package pl.lucky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lucky.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser_Id(Long ownerId);

}
