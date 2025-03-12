package project.model.dto.centerDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import project.model.entity.centerEntity.CenterEntity;

import java.io.File;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenterDto {
    private int centerno;
    private String name;
    private String address;
    private String contact;
    private String email;
    private String service;
    private String website;
    private String hours;
    private float rating;
    private int capacity;
    private int staff;
    private String photo; // 파일 경로를 저장하는 String
    private MultipartFile uploadFile; // 파일 업로드를 위한 필드


    public CenterEntity toEntity() {
        return CenterEntity.builder()
                .centerno(this.centerno)
                .name(this.name)
                .address(this.address)
                .contact(this.contact)
                .email(this.email)
                .service(this.service)
                .website(this.website)
                .hours(this.hours)
                .rating(this.rating)
                .capacity(this.capacity)
                .staff(this.staff)
                .photo(this.photo) // 파일 dto 문자열로 변환됨
                .build();
    }
}