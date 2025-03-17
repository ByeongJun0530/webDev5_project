package project.model.dto;

import lombok.*;
import project.model.entity.MessageEntity;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private int meno; // 쪽지 번호
    private String metitle; // 쪽지 제목
    private String mecontent; // 쪽지 내용
    private int sendermno; // 송신자
    private int receivermno; // 수신자

    // 화면 표시
    private String sendname; // 송신자 이름/기관명
    private String receivername; // 수신자 이름/기관명

    // dto -> entity
    public MessageEntity toEntity() {
        return MessageEntity.builder()
                .meno(this.meno)
                .metitle(this.metitle)
                .mecontent(this.mecontent)
                .build();
    }
}
