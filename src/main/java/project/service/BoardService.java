package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.dto.BoardDto;
import project.model.mapper.BoardMapper;

import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired private BoardMapper boardMapper;

    // 게시물 등록
    public boolean boardWrite(BoardDto boardDto){
        return boardMapper.boardWrite(boardDto);
    }
    // 게시물 전체 조회
    public List<BoardDto> boardFindAll(){
        return boardMapper.boardFindAll();
    }
    // 게시물 개별 조회
    public BoardDto boardFind(int bno){
        return boardMapper.boardFind(bno);
    }
    // 게시물 수정
    public boolean boardUpdate(BoardDto boardDto){
        return boardMapper.boardUpdate(boardDto);
    }
    // 게시물 삭제
    public boolean boardDelete(int bno){
        return boardMapper.boardDelete(bno);
    }

    // 댓글 쓰기
    public boolean replyWrite(Map<String, String> replyDto){
        return boardMapper.replyWrite(replyDto);
    }
    // 특정 게시물 댓글 조회
    public List<Map<String, String>> replyFindAll(int bno){
        return boardMapper.replyFindAll(bno);
    }

}
