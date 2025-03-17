package project.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.model.dto.BoardDto;
import project.model.dto.ReplyDto;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시물 등록
    boolean boardWrite(BoardDto boardDto);
    // 게시물 전체 조회
    List<BoardDto> boardFindAll(@Param("limit") int limit, @Param("offset") int offset,
                                @Param("keyword") String keyword, @Param("cno") Integer cno);
    // 전체 게시물 개수 조회
    int countBoards(String keyword, Integer cno);
    // 게시물 개별 조회
    BoardDto boardFind(int bno);
    // 게시물 수정
    boolean boardUpdate(BoardDto boardDto);
    // 게시물 삭제
    boolean boardDelete(int bno);
    // 댓글 쓰기
    boolean replyWrite(ReplyDto replyDto);
    // 댓글 출력
    List<ReplyDto> replyFindAll(@Param("bno") int bno);
}
