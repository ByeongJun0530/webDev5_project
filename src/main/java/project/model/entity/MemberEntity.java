package project.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter@Setter@ToString@Builder
@AllArgsConstructor@NoArgsConstructor
@Entity
@Table( name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno; // 회원 식별 번호

    @Column( nullable = false  , unique = true , columnDefinition = "varchar(30)")
    private String name; // 회원 이름
    @Column( nullable = false  , unique = true , columnDefinition = "varchar(30)")
    private String email; // 회원 아이디, 이메일
    @Column( nullable = false  , unique = true , columnDefinition = "varchar(100)")
    private String pwd; // 회원 비밀번호
    @Column( nullable = false  , unique = true , columnDefinition = "varchar(30)")
    private String phone; // 회원 전화번호

}
