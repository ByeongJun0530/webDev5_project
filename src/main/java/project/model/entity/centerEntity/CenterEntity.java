package project.model.entity.centerEntity;

import jakarta.persistence.*;
import lombok.*;
import project.model.dto.centerDto.CenterDto;
import project.model.entity.MemberEntity;

import java.io.File;

@Entity
@Table(name = "center")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CenterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int centerno; //pk

    @Column(nullable = false)
    private String name; //센터이름

    @Column(nullable = false)
    private String address; //센터주소

    @Column(nullable = false , columnDefinition = "varchar(25)")
    private String contact; //연락처

    @Column(nullable = true , columnDefinition = "varchar(25)")
    private String email; //이메일

    @Column(nullable = false)
    private String service; //제공서비스

    @Column(nullable = true)
    private String website; //사이트 url

    @Column(nullable = true)
    private String hours; //운영시간

    @Column(nullable = false)
    private float rating; //평점

    @Column(nullable = true)
    private int capacity; //수용인원

    @Column(nullable = false)
    private int staff; //직원수

    @Column(nullable = false)
    private String photo; //사진 (파일 타입으로 변경)

    public CenterDto toDto() {
        return CenterDto.builder()
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
                .photo(this.photo) // 파일 경로를 문자열로 변환
                .build();
    }
}