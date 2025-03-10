package project.model.dto.centerDto;

import lombok.*;
import project.model.entity.centerEntity.ReviewEntity;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int reviewno;
    private int centerno;
    private String reviewText;
    private double rating;

    public ReviewEntity toEntity() {
        return ReviewEntity.builder()
                .reviewno(this.reviewno)
                .centerno(this.centerno)
                .reviewText(this.reviewText)
                .rating(this.rating)
                .build();
    }
}
