package project.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ViewController {
    // 메인페이지
    @GetMapping("")
    public String index(){return "/html/main/index.html";}

    @GetMapping("/api")
    public String api(){return "/html/api/api.html";}

}
