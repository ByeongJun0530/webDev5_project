package project.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ViewController {
    // 메인페이지
    @GetMapping("/")
    public String index(){return "/html/main/index.html";}

    @GetMapping("/api")
    public String api(){return "/html/api/api.html";}

    // 회원가입
    @GetMapping("/member/signup")
    public String signupPage(){return "/html/member/signup.html";}
    //로그인
    @GetMapping("/member/login")
    public String login(){return "/html/member/login.html";}
    // 마이페이지
    @GetMapping("/member/info")
    public String memberInfo(){return "/html/member/info.html";}
    // 메세지
    @GetMapping("/view/message")
    public String viewMessage() {return "/message/message.html";}
    // 메세지 작성
    @GetMapping("/view/message/write")
    public String viewMessageWrite() {return "/message/write.html";}
}
