package pl.lucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lucky.data.TaskRepository;
import pl.lucky.model.Task;

import java.util.List;

@Controller
public class TaskController {

    private TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task){
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showAllTasks(Model model){
        model.addAttribute("task", new Task());
        List<Task> allTasks = taskRepository.findAll();
        model.addAttribute("allTasks", allTasks);
        return "index";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id){
        taskRepository.deleteById(id);
        return "redirect:/";
    }

}
