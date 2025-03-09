package project.model.dto.centerDto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import project.model.entity.centerEntity.CenterEntity;

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
    private String rating;
    private int capacity;
    private int staff;
    //private MultipartFile photo; //파일로 사용할 떄
    private String photo; // 수정 필요

    private double latitude;
    private double longitude;

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
                .latitude(this.latitude)
                .longitude(this.longitude)
                .photo(this.photo)
                .build();
    }
}
