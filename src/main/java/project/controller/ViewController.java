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

    // 게시물 목록 반환 함수
    @GetMapping("/board")
    public String board(){return "/html/main/board/board.html";}

    // 글쓰기 반환 함수
    @GetMapping("/board/write")
    public String boardWrite(){return "/html/main/board/write.html";}
}
