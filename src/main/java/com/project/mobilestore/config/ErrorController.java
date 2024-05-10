package com.project.mobilestore.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/page-not-found")
    private String pageNotFound() {
        return "page-not-found";
    }
}
