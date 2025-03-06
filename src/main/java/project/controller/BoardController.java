package project.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.model.dto.BoardDto;
import project.service.BoardService;

import java.util.List;

@RestController
public class BoardController {
    @Autowired private BoardService boardService;

    // 게시물 등록
    @PostMapping("/board/write")
    public boolean boardWrite(@RequestBody BoardDto boardDto){
        return boardService.boardWrite(boardDto);
    }
    // 게시물 전체 조회
    @GetMapping("/board/findall")
    public List<BoardDto> findAll(){
        return boardService.findAll();
    }
    // 게시물 개별 조회
    @GetMapping("/board/find")
    public BoardDto find(@RequestParam int bno){
        return boardService.find(bno);
    }
    // 게시물 수정
    @PutMapping("/board/update")
    public boolean boardUpdate(@RequestBody BoardDto boardDto){
        return boardService.boardUpdate(boardDto);
    }
    // 게시물 삭제
    @DeleteMapping("/board/delete")
    public boolean boardDelete(@RequestParam int bno){
        return boardService.boardDelete(bno);
    }

}
