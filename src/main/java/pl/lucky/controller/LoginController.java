package pl.lucky.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/registrationForm")
    public String secret() {
        return "registration-form";
    }

    @RequestMapping("/loginForm")
    public String loginForm() {
        return "login-form";
    }

    @RequestMapping("/logoutForm")
    public String logoutForm() {
        return "login-form";
    }

}
