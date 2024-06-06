package com.example.testcors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test2Controller {

    @GetMapping("/test2")
    public String test2() {
        return "test-html";
    }
}
