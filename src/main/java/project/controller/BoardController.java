package project.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.model.dto.BoardDto;
import project.model.dto.ReplyDto;
import project.service.BoardService;

import java.util.List;
import java.util.Map;

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
    public Map<String, Object> boardFindAll(@RequestParam(defaultValue = "1") int page, @RequestParam int pageSize,
                                            @RequestParam(required = false, defaultValue = "") String keyword,
                                            @RequestParam(required = false, defaultValue = "") Integer cno)
    {
        return boardService.pagedBoards(page, pageSize, keyword, cno);
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
    public boolean replyWrite(@RequestBody ReplyDto replyDto){
        return boardService.replyWrite(replyDto);
    }
    // 특정 게시물 댓글 조회
    @GetMapping("reply/findall.do")
    public List<ReplyDto> replyFindAll(@RequestParam int bno){
        return boardService.replyFindAll(bno);
    }

}
