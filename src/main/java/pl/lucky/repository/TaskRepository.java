package pl.lucky.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lucky.model.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUser_Id(Long ownerId, Pageable pageable);

    @Query("SELECT t FROM tasks t WHERE t.taskDeadline = :tomorrow")
    List<Task> findAllByTaskDeadline(@Param("tomorrow") LocalDate tomorrow);
}
