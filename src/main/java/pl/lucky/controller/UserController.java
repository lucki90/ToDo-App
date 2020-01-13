package pl.lucky.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lucky.model.User;
import pl.lucky.services.UserService;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "registration-form";
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute User user, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registration-form";
        } else {

            try {
                userService.addWithDefaultRole(user);
                return "registration-success";
            } catch (SQLIntegrityConstraintViolationException e) {
                String exceptionMessage = ((ConstraintViolationException) e.getCause().getCause()).getSQLException().toString();
                Pattern pattern = Pattern.compile("'(.*?)'");
                Matcher matcher = pattern.matcher(exceptionMessage);
                if (matcher.find()) {
                    bindingResult.rejectValue("globalError", "error.task", "'" + matcher.group(1) + "' is already taken.");
                } else {
                    bindingResult.rejectValue("globalError", "error.task", "Something went wrong, try fill form once again.");
                }
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
                return "registration-form";
            }
        }
    }
}
