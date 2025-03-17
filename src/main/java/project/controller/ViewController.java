package project.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.service.MemberService;

@Controller
public class ViewController {
    // 메인페이지
    @GetMapping("/")
    public String index(){return "/html/main/index.html";}

    @GetMapping("/api")
    public String api(){return "/html/api/api.html";}

    // 게시물 목록 반환 함수
    @GetMapping("/board")
    public String board(){return "/html/main/board/board.html";}

    // 글쓰기 반환 함수
    @GetMapping("/board/write")
    public String boardWrite(){return "/html/main/board/write.html";}

    // 개별 조회 반환
    @GetMapping("/board/find")
    public String boardFind(){return "/html/main/board/find.html";}
    // 회원가입
    @GetMapping("/member/signup")
    public String signupPage(){return "/html/member/signup.html";}
    //로그인
    @GetMapping("/member/login")
    public String login(){return "/html/member/login.html";}
    // 비밀번호 찾기 페이지
    @GetMapping("/member/find-password")
    public String findPassword(){return "/html/member/find-password.html";}
    // 마이페이지
    @GetMapping("/member/info")
    public String memberInfo(){return "/html/member/info.html";}

    // 메세지 관련 화면
    @Autowired
    MemberService memberService;
// 메세지 작성 페이지
    @GetMapping("/message/compose")
    public String messageCompose() {
        // 로그인 여부 확인
        if (memberService.getSession() == null) {
            return "redirect:/member/login";
        }
        return "/html/message/compose.html";
    }

    // 받은 메세지함 페이지
    @GetMapping("/message/received")
    public String receivedMessages() {
        // 로그인 여부 확인
        if (memberService.getSession() == null) {
            return "redirect:/member/login";
        }
        return "/html/message/received.html";
    }

    // 보낸 메세지함 페이지
    @GetMapping("/message/sent")
    public String sentMessages() {
        // 로그인 여부 확인
        if (memberService.getSession() == null) {
            return "redirect:/member/login";
        }
        return "/html/message/sent.html";
    }

    // 메세지 상세 보기 페이지
    @GetMapping("/message/detail")
    public String messageDetail() {
        // 로그인 여부 확인
        if (memberService.getSession() == null) {
            return "redirect:/member/login";
        }
        return "/html/message/detail.html";
    }

    // 헤더 조각 제공 (추가됨)
    @GetMapping("/fragment/header")
    public String header() {
        return "/fragment/header.html";
    }

}
