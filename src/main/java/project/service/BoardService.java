package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.dto.BoardDto;
import project.model.dto.ReplyDto;
import project.model.mapper.BoardMapper;

import java.util.HashMap;
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
    /*
    public List<BoardDto> boardFindAll(){
        return boardMapper.boardFindAll();
    }
     */
    public Map<String, Object> pagedBoards(int page, int pageSize, String keyword, Integer cno){
        int totalBoards = boardMapper.countBoards(keyword, cno);
        int totalPages = (int)Math.ceil((double) totalBoards / pageSize);
        int offset = (page - 1) * pageSize;

        List<BoardDto> boards = boardMapper.boardFindAll(pageSize, offset, keyword, cno);

        // 게시글에 해당하는 댓글 추가
        for(BoardDto board : boards){
            List<ReplyDto> replies = boardMapper.replyFindAll(board.getBno());
            board.setReplylist(replies);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("boards", boards);
        result.put("totalBoards", totalBoards);
        result.put("totalPages", totalPages);
        result.put("currentPage", page);
        result.put("pageSize", pageSize);


        return result;
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
    public boolean replyWrite(ReplyDto replyDto){
        return boardMapper.replyWrite(replyDto);
    }
    // 특정 게시물 댓글 조회
    public List<ReplyDto> replyFindAll(int bno){
        return boardMapper.replyFindAll(bno);
    }

}
