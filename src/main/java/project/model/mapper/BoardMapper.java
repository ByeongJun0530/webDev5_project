package project.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.model.dto.BoardDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    // 게시물 등록
    boolean boardWrite(BoardDto boardDto);
    // 게시물 전체 조회
    List<BoardDto> boardFindAll(@Param("limit") int limit, @Param("offset") int offset);
    // 전체 게시물 개수 조회
    int countBoards();
    // 게시물 개별 조회
    BoardDto boardFind(int bno);
    // 게시물 수정
    boolean boardUpdate(BoardDto boardDto);
    // 게시물 삭제
    boolean boardDelete(int bno);
    // 댓글 쓰기
    boolean replyWrite(Map<String, String> replyDto);
    // 댓글 출력
    List<Map<String,String>> replyFindAll(int bno);
}
