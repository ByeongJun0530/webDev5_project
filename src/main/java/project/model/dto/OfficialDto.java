package project.model.dto;

import lombok.*;
import project.model.entity.OfficialEntity;

@Setter@Getter@Builder
@NoArgsConstructor@AllArgsConstructor
public class OfficialDto {
    //사업자 번호
    //센터 사업자 번호 들어가는것에 따라 달라짐
    private int ono;
    //비밀번호
    private String opwd;
    //이메일
    private String oemail;
    //전화번호
    private String ophone;
    //가입날짜
    private String cdate;


    //to entity
    OfficialEntity toEntity(){
        return OfficialEntity.builder()
                .opwd(this.opwd)
                .ophone(this.ophone)
                .oemail(this.oemail)
                .build();
    }
}
