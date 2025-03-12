package project.model.entity.centerEntity;

import jakarta.persistence.*;
import lombok.*;
import project.model.dto.centerDto.ReviewDto;
import project.model.entity.MemberEntity;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewno; //pk

    @Column(nullable = false)
    private String reviewText; // 후기 내용

    @Column(nullable = false)
    private double rating; // 후기 평점 (5.0 이 아니라 1~5면 int로 수정)

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "centerno")
    private CenterEntity centerEntity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;

    public ReviewDto toDto() {
        return ReviewDto.builder()
                .reviewno(this.reviewno)
                .reviewText(this.reviewText)
                .rating(this.rating)
                .centerno(this.centerEntity.getCenterno())
                .mno(this.memberEntity.getMno())
                .build();
    }

}
