package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.model.dto.MemberDto;
import project.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired private MemberService memberService;

    // 회원가입
    @PostMapping("signup.do")
    public boolean signup(@RequestBody MemberDto memberDto){
        return memberService.signup(memberDto);
    }

    // 현재 로그인된 회원 아이디 http 매핑
    @GetMapping("login/id.do")
    public String loginId() {
        return memberService.getSession();
    }

    // 내정보 조회
    @GetMapping("/myinfo.do")
    public MemberDto myInfo(){
        return memberService.getMyInfo();
    }

    // 회원 탈퇴
    @DeleteMapping("/delete.do")
    public boolean myDelete(){
        return memberService.myDelete();
    }

    // 회원정보 수정
    @PutMapping("update.do")
    public boolean myUdate(@RequestBody MemberDto memberDto){
        return memberService.myUpdate(memberDto);
    }

}
