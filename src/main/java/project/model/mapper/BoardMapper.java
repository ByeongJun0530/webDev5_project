package project.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.model.dto.BoardDto;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시물 등록
    boolean save(BoardDto boardDto);
    // 게시물 전체 조회
    List<BoardDto> findAll();
    // 게시물 개별 조회
    BoardDto find(int bno);
    // 게시물 수정
    boolean update(BoardDto boardDto);
    // 게시물 삭제
    boolean delete(int bno);
}
