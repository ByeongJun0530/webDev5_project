package project.service.centerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.dto.centerDto.ReviewDto;
import project.model.entity.centerEntity.ReviewEntity;
import project.model.repository.centerRepository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    // 리뷰 등록
    public boolean upload(ReviewDto reviewDto) {
        ReviewEntity reviewEntity = reviewDto.toEntity();
        reviewRepository.save(reviewEntity);
        return true;
    }

    // 리뷰 전체 조회
    public List<ReviewDto> findall() {
        List<ReviewEntity> reviews = reviewRepository.findAll();
        return reviews.stream().map(ReviewEntity::toDto).collect(Collectors.toList());
    }

    // 개별 리뷰 조회
    public ReviewDto find(int reviewno) {
        ReviewEntity review = reviewRepository.findById(reviewno).orElse(null);
        if (review != null) {
            return review.toDto();
        }
        return null;
    }

    // 리뷰 정보 수정
    public boolean update(ReviewDto reviewDto) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewDto.getReviewno()).orElse(null);
        if (reviewEntity != null) {
            ReviewEntity updatedReview = reviewDto.toEntity();
            updatedReview.setReviewno(reviewDto.getReviewno());
            reviewRepository.save(updatedReview);
            return true;
        }
        return false;
    }

    // 리뷰 삭제
    public boolean delete(int reviewno) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewno).orElse(null);
        if (reviewEntity != null) {
            reviewRepository.delete(reviewEntity);
            return true;
        }
        return false;
    }

    public List<ReviewDto> findByCenter(int centerno) {
        return reviewRepository.findByCenterEntity_Centerno(centerno)
                .stream()
                .map(ReviewEntity::toDto)
                .collect(Collectors.toList());
    }

}
