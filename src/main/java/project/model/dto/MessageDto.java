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
    private String sendmid; // 송신자 이메일
    private String receivermid; // 수신자 이메일

    // dto -> entity
    public MessageEntity toEntity() {
        return MessageEntity.builder()
                .meno(this.meno)
                .metitle(this.metitle)
                .mecontent(this.mecontent)
                .build();
    }
}