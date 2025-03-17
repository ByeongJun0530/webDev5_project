package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.dto.MessageDto;
import project.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired private MessageService messageService;

    // 보내기
    @PostMapping("/send.do")
    public ResponseEntity<Boolean> sendMessage(@RequestBody MessageDto messageDto) {
        boolean result = messageService.send(messageDto);
        return ResponseEntity.ok(result);
    }

    // 받은 메세지 조회
    @GetMapping("/receive/find.do")
    public ResponseEntity<List<MessageDto>> findReceiveMessage(@RequestParam int receivermno) {
        List<MessageDto> messages = messageService.FindReceiverMessage(receivermno);
        return ResponseEntity.ok(messages);
    }

    // 보낸 메세지 조회
    @GetMapping("send/find.do")
    public ResponseEntity<List<MessageDto>> findSendMessage(@RequestParam int sendermno) {
        List<MessageDto> messages = messageService.FindSendMessage(sendermno);
        return ResponseEntity.ok(messages);
    }

    // 보낸 메세지 삭제
    @DeleteMapping("/send/delete.do")
    public ResponseEntity<Boolean> deleteSendMessage(@RequestParam int meno, @RequestParam int mno) {
        boolean result = messageService.deleteSendMessage(meno, mno);
        if(result){
            return ResponseEntity.ok(true);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    // 받은 메세지 삭제
    @DeleteMapping("/receiver/delete.do")
    public ResponseEntity<Boolean> deleteReceiveMessage(@RequestParam int meno, @RequestParam int mno){
        boolean result = messageService.deleteByReceiver(meno, mno);
        if (result) {
            return ResponseEntity.ok(true);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    @GetMapping("/mno.do")
    public int mno() {
        return messageService.mno();
    }

    @GetMapping("/message/find-mno/{memail}")
    public ResponseEntity<Integer> findMnoById(@PathVariable String memail) {
        Integer mno = messageService.findMnoById(memail);
        if(mno != null) {
            return ResponseEntity.ok(mno);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 메시지 상세 조회
    @GetMapping("/detail.do")
    public ResponseEntity<MessageDto> getMessageDetail(@RequestParam int meno, @RequestParam int mno) {
        MessageDto message = messageService.findMessageById(meno, mno);
        if(message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //

}