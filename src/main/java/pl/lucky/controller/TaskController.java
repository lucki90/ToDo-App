package pl.lucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lucky.model.Task;
import pl.lucky.services.TaskService;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult result) {
        try {
            taskService.save(task);
        } catch (ConstraintViolationException ex) {
            if (result.hasErrors()) {
                    List<ObjectError> errors = result.getAllErrors();
                    errors.forEach(e -> System.out.println(e.getDefaultMessage()));

            }
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String showAllTasks(Model model) {
        model.addAttribute("task", new Task());
        List<Task> allTasks = taskService.findAll();
        model.addAttribute("allTasks", allTasks);
        return "index";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/";
    }

}
