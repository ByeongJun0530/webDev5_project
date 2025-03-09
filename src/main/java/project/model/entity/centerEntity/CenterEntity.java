package project.model.entity.centerEntity;


import jakarta.persistence.*;
import lombok.*;
import project.model.dto.centerDto.CenterDto;

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

    @Column(nullable = false)
    private String contact; //연락처

    @Column(nullable = false)
    private String email; //이메일

    @Column(nullable = false)
    private String service; //제공서비스? (축소)

    @Column(nullable = false)
    private String website; //사이트 url

    @Column(nullable = false)
    private String hours; //운영시간

    @Column(nullable = false)
    private String rating; //평점

    @Column(nullable = false)
    private int capacity; //수용인원

    @Column(nullable = false)
    private int staff; //직원수

    @Column(nullable = false)
    private String photo; //사진 (사진 파일로 할지 이미지 주소로 할지 정해야됨)

    @Column(nullable = false)
    private double latitude; //위도(api)

    @Column(nullable = false)
    private double longitude; //경도(api)

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
                .photo(this.photo)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
