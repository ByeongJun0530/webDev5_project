package project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.model.dto.MemberDto;
import project.model.dto.MessageDto;
import project.service.MemberService;
import project.service.MessageService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    @Autowired private MessageService messageService;
    @Autowired private MemberService memberService;

    // 메시지 페이지 이동
    @GetMapping("")
    public String messagePage(Model model) {
        // 현재 로그인한 회원 정보 조회
        MemberDto loginMember = memberService.getMyInfo();
        if (loginMember == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        int mno = loginMember.getMno();

        // 받은 메시지 목록
        List<MessageDto> receivedMessages = messageService.FindReceiverMessage(mno);
        model.addAttribute("receivedMessages", receivedMessages);

        // 보낸 메시지 목록
        List<MessageDto> sentMessages = messageService.FindSendMessage(mno);
        model.addAttribute("sentMessages", sentMessages);

        model.addAttribute("loginInfo", loginMember);

        return "message/messageMain"; // 메시지 메인 페이지 반환
    }

    // 새 메시지 페이지 이동
    @GetMapping("/write")
    public String writeMessagePage(@RequestParam(value = "receiver", required = false) String receiver, Model model) {
        // 현재 로그인한 회원 정보 조회
        MemberDto loginMember = memberService.getMyInfo();
        if (loginMember == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 수신자 정보가 있는 경우 모델에 추가
        if (receiver != null && !receiver.isEmpty()) {
            Integer receiverMno = messageService.findMnoById(receiver);
            if (receiverMno != null) {
                model.addAttribute("receiverMemail", receiver);
                model.addAttribute("receiverMno", receiverMno);
            }
        }

        model.addAttribute("loginInfo", loginMember);

        return "message/messageWrite"; // 메시지 작성 페이지 반환
    }

    // 메시지 전송 처리
    @PostMapping("/send")
    @ResponseBody
    public boolean sendMessage(@RequestBody MessageDto messageDto) {
        return messageService.send(messageDto);
    }

    // 받은 메시지 상세 조회
    @GetMapping("/received/{meno}")
    public String receivedMessageDetail(@PathVariable int meno, Model model) {
        // 현재 로그인한 회원 정보 조회
        MemberDto loginMember = memberService.getMyInfo();
        if (loginMember == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 받은 메시지 목록에서 해당 메시지 찾기
        List<MessageDto> receivedMessages = messageService.FindReceiverMessage(loginMember.getMno());
        MessageDto messageDetail = null;

        for (MessageDto message : receivedMessages) {
            if (message.getMeno() == meno) {
                messageDetail = message;
                break;
            }
        }

        if (messageDetail == null) {
            return "redirect:/message"; // 메시지가 없으면 메시지 목록으로 리다이렉트
        }

        model.addAttribute("message", messageDetail);
        model.addAttribute("messageType", "received");
        model.addAttribute("loginInfo", loginMember);

        return "message/messageDetail"; // 메시지 상세 페이지 반환
    }

    // 보낸 메시지 상세 조회
    @GetMapping("/sent/{meno}")
    public String sentMessageDetail(@PathVariable int meno, Model model) {
        // 현재 로그인한 회원 정보 조회
        MemberDto loginMember = memberService.getMyInfo();
        if (loginMember == null) {
            return "redirect:/member/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 보낸 메시지 목록에서 해당 메시지 찾기
        List<MessageDto> sentMessages = messageService.FindSendMessage(loginMember.getMno());
        MessageDto messageDetail = null;

        for (MessageDto message : sentMessages) {
            if (message.getMeno() == meno) {
                messageDetail = message;
                break;
            }
        }

        if (messageDetail == null) {
            return "redirect:/message"; // 메시지가 없으면 메시지 목록으로 리다이렉트
        }

        model.addAttribute("message", messageDetail);
        model.addAttribute("messageType", "sent");
        model.addAttribute("loginInfo", loginMember);

        return "message/messageDetail"; // 메시지 상세 페이지 반환
    }

    // 보낸 메시지 삭제
    @DeleteMapping("/sent/{meno}")
    @ResponseBody
    public boolean deleteSentMessage(@PathVariable int meno) {
        MemberDto loginMember = memberService.getMyInfo();
        if (loginMember == null) {
            return false; // 로그인되지 않은 경우
        }

        return messageService.deleteSendMessage(meno, loginMember.getMno());
    }

    // 받은 메시지 삭제
    @DeleteMapping("/received/{meno}")
    @ResponseBody
    public boolean deleteReceivedMessage(@PathVariable int meno) {
        MemberDto loginMember = memberService.getMyInfo();
        if (loginMember == null) {
            return false; // 로그인되지 않은 경우
        }

        return messageService.deleteReceiverMessage(meno, loginMember.getMno());
    }

    // 회원 이메일로 회원번호 조회 (AJAX용)
    @GetMapping("/findMember")
    @ResponseBody
    public Integer findMemberByMemail(@RequestParam String memail) {
        return messageService.findMnoById(memail);
    }

    // 회원 번호 조회 (현재 로그인한 사용자)
    @GetMapping("/currentMno")
    @ResponseBody
    public int getCurrentMno() {
        return messageService.mno();
    }
}