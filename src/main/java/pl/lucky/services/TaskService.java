package pl.lucky.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lucky.model.Task;
import pl.lucky.repository.TaskRepository;
import pl.lucky.repository.UserRepository;
import pl.lucky.security.AccessFilter;

import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Collections;
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

    public Page<Task> findAllPaginated(Pageable pageable){
        List<Task> tasks = findAll();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Task> list;

        if (tasks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tasks.size());
            list = tasks.subList(startItem, toIndex);
        }
        return new PageImpl<Task>(list, PageRequest.of(currentPage,pageSize),tasks.size());
    }

    public List<Task> findAll() {
        if (accessFilter.getOwnerRole().getRole().contains("ADMIN")) {
            return taskRepository.findAllByOrderByTaskCreateTimeDesc();
        }
        return taskRepository.findByUser_IdOrderByTaskCreateTimeDesc(accessFilter.getOwnerId());
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    //Method will be use for automatic sending mail to all users
    public List<Task> findTasksForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return taskRepository.findAllByTaskDeadline(tomorrow);
    }

}
