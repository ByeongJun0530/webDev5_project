package project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import project.model.dto.MemberDto;

@Getter@Setter@ToString@Builder
@AllArgsConstructor@NoArgsConstructor
@Entity
@Table( name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno; // 회원 식별 번호

    @Column( nullable = false  , columnDefinition = "varchar(30)")
    private String mname; // 회원 이름
    @Column( nullable = false  , unique = true , columnDefinition = "varchar(30)")
    private String memail; // 회원 아이디, 이메일
    @Column( nullable = false   , columnDefinition = "varchar(100)")
    private String mpwd; // 회원 비밀번호
    @Column( nullable = false  , unique = true , columnDefinition = "varchar(30)")
    private String mphone; // 회원 전화번호

    // entity -> dto 변환 함수
    public MemberDto toDto(){
        return MemberDto.builder()
                .mno(this.mno)
                .memail(this.memail)
                .mname(this.mname)
                .mpwd(this.mpwd)
                .mphone(this.mphone)
                .build();
    }

}
