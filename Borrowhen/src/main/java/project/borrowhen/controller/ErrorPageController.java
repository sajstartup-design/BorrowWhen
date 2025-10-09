package project.borrowhen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

    @GetMapping("/error/401")
    public String unauthorized() {
        return "error/401"; // resolves to src/main/resources/templates/error/401.html
    }

    @GetMapping("/error/403")
    public String forbidden() {
        return "error/401"; 
    }
}
