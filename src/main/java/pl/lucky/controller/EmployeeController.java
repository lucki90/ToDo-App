package pl.lucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lucky.model.Employee;
import pl.lucky.model.Task;
import pl.lucky.repository.EmployeeRepository;
import pl.lucky.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/employees")
    public String getEmployees(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                               Model model) {
        Page<Task> taskPage = taskRepository.findAll(pageable);
        List<Sort.Order> sortOrders = taskPage.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order order = sortOrders.get(0);
            model.addAttribute("sortProperty", order.getProperty());
            model.addAttribute("sortDesc", order.getDirection() == Sort.Direction.DESC);
        }
        model.addAttribute("taskPage", taskPage);
        return "employee-page";
    }
}

