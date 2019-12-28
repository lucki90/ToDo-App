package pl.lucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.lucky.model.Task;
import pl.lucky.services.TaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TaskController {

    private TaskService taskService;
    private int globalCurrentPage;
    private int globalPageSize;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult result, Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {

        if (result.hasErrors()) {
            getPaginatedTasks(model, page, size);
            return "index";
        }
        taskService.save(task);
        return "redirect:/";
    }

    @GetMapping("/")
    public String showAllTasks(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        Task task = new Task();
        model.addAttribute(task);
        getPaginatedTasks(model, page, size);
        return "index";
    }

    private void getPaginatedTasks(Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size) {
        int currentPage = getCurrentPage(page);
        int pageSize = getPageSize(size);

        Page<Task> taskPage = taskService.findAllPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("taskPage", taskPage);

        int totalPages = taskPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

    private int getPageSize(@RequestParam("size") Optional<Integer> size) {
        int pageSize;
        if (size.isPresent()) {
            pageSize = size.orElse(5);
            this.globalPageSize = pageSize;
        } else if (this.globalPageSize != 0) {
            pageSize = this.globalPageSize;
        } else {
            pageSize = 5;
        }
        return pageSize;
    }

    private int getCurrentPage(@RequestParam("page") Optional<Integer> page) {
        int currentPage;
        if (page.isPresent()) {
            currentPage = page.orElse(1);
            this.globalCurrentPage = currentPage;
        } else if (this.globalCurrentPage != 0) {
            currentPage = this.globalCurrentPage;
        } else {
            currentPage = 1;
        }
        return currentPage;
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/";
    }

}
