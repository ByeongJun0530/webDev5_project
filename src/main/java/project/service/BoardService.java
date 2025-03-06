package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import project.model.dto.BoardDto;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardService boardService;

    // 게시물 등록
    public boolean boardWrite(BoardDto boardDto){
        return boardService.boardWrite(boardDto);
    }
    // 게시물 전체 조회
    public List<BoardDto> findAll(){
        return boardService.findAll();
    }
    // 게시물 개별 조회
    public BoardDto find(int bno){
        return boardService.find(bno);
    }
    // 게시물 수정
    public boolean boardUpdate(BoardDto boardDto){
        return boardService.boardUpdate(boardDto);
    }
    // 게시물 삭제
    public boolean boardDelete(int bno){
        return boardService.boardDelete(bno);
    }
}
