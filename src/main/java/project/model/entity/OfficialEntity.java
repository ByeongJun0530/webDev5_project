package project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import project.model.dto.OfficialDto;

@Getter@Setter@Builder
@AllArgsConstructor@NoArgsConstructor
@Entity @Table(name = "official")
public class OfficialEntity {

    //사업자 번호
    //센터 사업자 번호에 따라 달라질 예정
    @Id
    private int ono;

    //비밀번호
    @Column(columnDefinition = "varchar(30)",nullable = false)
    private String opwd;

    //이메일
    @Column(columnDefinition = "varchar(30)",nullable = false)
    private String oemail;

    //전화번호
    @Column(columnDefinition = "varchar(30)",nullable = false)
    private String ophone;


    OfficialDto toDto(){
        return OfficialDto.builder()
                .opwd(this.opwd)
                .ophone(this.ophone)
                .oemail(this.oemail)
                .build();
    }

}
