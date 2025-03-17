package project.model.dto;

import lombok.*;

@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class CateDto {
    private int cno; // 카테고리 번호
    private String cname; // 카테고리 이름
}
