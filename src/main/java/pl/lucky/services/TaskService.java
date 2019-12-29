package pl.lucky.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lucky.model.Task;
import pl.lucky.repository.TaskRepository;
import pl.lucky.repository.UserRepository;
import pl.lucky.security.AccessFilter;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    private AccessFilter accessFilter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        task.setUser(userRepository.findById(accessFilter.getOwnerId()).get());
        validator.validate(task);
        taskRepository.save(task);
    }

    public Page<Task> findAllPaginated(Pageable pageable) {
        if (accessFilter.getOwnerRole().getRole().contains("ADMIN")) {
            return taskRepository.findAll(pageable);
        }
        return taskRepository.findByUser_Id(accessFilter.getOwnerId(), pageable);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    //Method is used for automatic sending mail to all users
    public List<Task> findTasksForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return taskRepository.findAllByTaskDeadline(tomorrow);
    }

}
