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

    // 로그인
    @PostMapping("/login")
    public boolean login(@RequestBody MemberDto memberDto){return memberService.login(memberDto);}

    // 로그아웃
    @GetMapping("/logout")
    public boolean logout(){return memberService.deleteSession();}

    // 현재 로그인된 회원 조회
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

    // 비밀번호 찾기 (임시 비밀번호 생성)
    @PostMapping("/find-password.do")
    public boolean findPassword(@RequestBody MemberDto memberDto) {
        return memberService.resetPassword(memberDto.getMemail());
    }

}
