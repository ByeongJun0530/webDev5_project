package project.model.dto;

import lombok.*;

@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberDto {
    private int mno; // 회원식별번호
    private String email; // 회원 이메일, 아이디
    private String name; // 회원 이름
    private String pwd; // 회원 비번
    private String phone; // 회원 전화번호
}
