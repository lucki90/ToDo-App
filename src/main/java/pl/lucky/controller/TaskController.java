package pl.lucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.lucky.model.Task;
import pl.lucky.services.TaskService;

import java.util.List;

@Controller
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task) {
        taskService.save(task);

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
