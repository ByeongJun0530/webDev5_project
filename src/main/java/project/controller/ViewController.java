package project.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    // 메인페이지
    @GetMapping("")
    public String index(){return "/html/main/index.html";}
}
