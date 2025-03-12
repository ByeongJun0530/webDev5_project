package project.model.dto.centerDto;

import lombok.*;
import project.model.entity.centerEntity.CenterEntity;
import project.model.entity.centerEntity.ReviewEntity;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int reviewno;
    private String reviewText;
    private double rating;

    private int centerno;
    private int mno;

    public ReviewEntity toEntity() {
        return ReviewEntity.builder()
                .reviewno(this.reviewno)
                .reviewText(this.reviewText)
                .rating(this.rating)
                .build();
    }
}
