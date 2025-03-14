package project.model.dto;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.model.entity.MemberEntity;

@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberDto {
    private int mno;
    private String mname;
    private String memail;
    private String mpwd;
    private String mphone;

    // dto --> entity
    public MemberEntity toEntity(){
        // 비밀번호 null 체크 추가
        String encodedPassword = this.mpwd != null ?
                new BCryptPasswordEncoder().encode(this.mpwd) : null;

        return MemberEntity.builder()
                .mno(this.mno)
                .memail(this.memail)
                // .mpwd(this.mpwd) // 암호화 전
                .mpwd(new BCryptPasswordEncoder().encode(this.mpwd))
                .mname(this.mname)
                .mphone(this.mphone)
                .build();
    }
}
