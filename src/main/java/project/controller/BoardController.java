package project.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.model.dto.BoardDto;
import project.service.BoardService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class BoardController {
    @Autowired private BoardService boardService;

    // 게시물 등록
    @PostMapping("/board/write.do")
    public boolean boardWrite(@RequestBody BoardDto boardDto){
        return boardService.boardWrite(boardDto);
    }
    // 게시물 전체 조회
    @GetMapping("/board/findall.do")
    public Map<String, Object> boardFindAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize){
        return  boardService.pagedBoards(page, pageSize);
    }
    // 게시물 개별 조회
    @GetMapping("/board/find.do")
    public BoardDto boardFind(@RequestParam int bno){
        return boardService.boardFind(bno);
    }
    // 게시물 수정
    @PutMapping("/board/update.do")
    public boolean boardUpdate(@RequestBody BoardDto boardDto){
        return boardService.boardUpdate(boardDto);
    }
    // 게시물 삭제
    @DeleteMapping("/board/delete.do")
    public boolean boardDelete(@RequestParam int bno){
        return boardService.boardDelete(bno);
    }

    // 댓글 쓰기
    @PostMapping("/reply/write.do")
    public boolean replyWrite(@RequestBody Map<String, String> replyDto){
        return boardService.replyWrite(replyDto);
    }
    // 특정 게시물 댓글 조회
    @GetMapping("reply/findall.do")
    public List<Map<String, String>> replyFindAll(@RequestParam int bno){
        return boardService.replyFindAll(bno);
    }

}
