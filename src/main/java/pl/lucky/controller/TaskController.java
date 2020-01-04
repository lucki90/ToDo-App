package pl.lucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lucky.model.Task;
import pl.lucky.services.TaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todo-app")
public class TaskController {

    private TaskService taskService;
    private Pageable pageable;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
    public String saveTask(@Valid @ModelAttribute Task task, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes,
                           @PageableDefault(size = 5, sort = "taskCreateTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        if (result.hasErrors()) {
            getPaginatedTasks(model, this.pageable != null ? this.pageable : pageable);
            return "index";
        }
        taskService.save(task);
        addRedirectAttributes(redirectAttributes, pageable);
        return "redirect:/todo-app/";
    }

    @GetMapping("/")
    public String showAllTasks(Model model, @PageableDefault(size = 5, sort = "taskCreateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        this.pageable = pageable;
        Task task = new Task();
        model.addAttribute("task", task);
        getPaginatedTasks(model, pageable);
        return "index";
    }

    private void getPaginatedTasks(Model model, Pageable pageable) {
        Page<Task> taskPage = taskService.findAllPaginated(pageable);


        List<Sort.Order> sortOrders = taskPage.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        model.addAttribute("taskPage", taskPage);
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             @PageableDefault(size = 5, sort = "taskCreateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        taskService.delete(id);
        addRedirectAttributes(redirectAttributes, pageable);
        return "redirect:/todo-app/";
    }

    private RedirectAttributes addRedirectAttributes(RedirectAttributes redirectAttributes, Pageable pageable) {
        redirectAttributes.addAttribute("page", this.pageable != null ? this.pageable.getPageNumber() : pageable.getPageNumber());
        redirectAttributes.addAttribute("size", this.pageable != null ? this.pageable.getPageSize() : pageable.getPageSize());
        redirectAttributes.addAttribute("sort", this.pageable != null ? this.pageable.getSort().toString().replaceFirst(": ", ",") : pageable.getSort().toString().replaceFirst(": ", ","));
        return redirectAttributes;
    }

}
