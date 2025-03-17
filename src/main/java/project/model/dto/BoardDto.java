package project.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class BoardDto {
    private int bno; // 게시물 번호
    private String btitle; // 게시물 제목
    private String bcontent; // 게시물 내용
    private int mno; // 회원번호 fk
    private String memail; // 회원id fk
    private int cno; // 카테고리 번호 fk
    private String cname; // 카테고리 이름 fk
    private LocalDate cdate; // 등록일자
    private LocalDate mdate; // 수정일자

    private List<ReplyDto> replylist; // 댓글
}
