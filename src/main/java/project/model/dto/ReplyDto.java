package project.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private int rno;       // 댓글 번호
    private String memail;    // 회원 ID
    private String rcontent; // 댓글 내용
    private int bno;       // 게시글 번호 FK
    private LocalDate cdate; // 댓글 작성일
}
