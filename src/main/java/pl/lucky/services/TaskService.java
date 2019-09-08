package pl.lucky.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lucky.model.Task;
import pl.lucky.repository.TaskRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void save(Task task) {
        try {
            validator.validate(task);
            taskRepository.save(task);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                System.out.printf("%s - %s(%s)\n",
                        violation.getPropertyPath(),
                        violation.getInvalidValue(),
                        violation.getMessage());
            }
        }
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }


}
