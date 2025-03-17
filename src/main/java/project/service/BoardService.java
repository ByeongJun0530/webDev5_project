package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.dto.BoardDto;
import project.model.dto.ReplyDto;
import project.model.entity.MemberEntity;
import project.model.mapper.BoardMapper;
import project.model.repository.MemberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired private BoardMapper boardMapper;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    // 게시물 등록
    public boolean boardWrite(BoardDto boardDto) {
        if (memberService.getSession() != null) {
            MemberEntity member = memberRepository.findByMemail(memberService.getSession());

            if (member != null) {
                boardDto.setMemail(member.getMemail()); // 이메일 설정
                boardDto.setMno(member.getMno());       // 회원 번호 설정 (중요!)
            } else {
                System.out.println("회원 정보를 찾을 수 없습니다.");
                return false;
            }
        } else {
            System.out.println("로그인 정보가 없습니다.");
            return false;
        }

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
        if (boardMapper.boardFind(bno).getMemail() == memberService.getSession()){
            System.out.println("삭제 성공");
        }else {
            System.out.println("삭제 실패 : 본인의 게시물만 삭제 가능합니다.");
        }
        return boardMapper.boardDelete(bno);
    }

    // 댓글 쓰기
    public boolean replyWrite(ReplyDto replyDto){
        if (memberService.getSession() != null){
            String memail = memberRepository.findByMemail(memberService.getSession()).getMemail();
            replyDto.setMemail(memail);
        }else {
            System.out.println("로그인 시 이용 가능합니다.");
            return false;
        }
        return boardMapper.replyWrite(replyDto);
    }
    // 특정 게시물 댓글 조회
    public List<ReplyDto> replyFindAll(int bno){
        return boardMapper.replyFindAll(bno);
    }

}
