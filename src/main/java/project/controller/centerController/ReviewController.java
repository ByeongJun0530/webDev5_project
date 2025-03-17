package project.controller.centerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.model.dto.centerDto.ReviewDto;
import project.service.centerService.ReviewService;

import java.util.List;

@RestController
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/review/upload.do")
    public boolean upload(@RequestBody ReviewDto reviewDto) {
        return reviewService.upload(reviewDto);
    }

    // 리뷰 전체 조회
    @GetMapping("/review/findall.do")
    public List<ReviewDto> findall() {
        return reviewService.findall();
    }

    // 개별 리뷰 조회
    @GetMapping("/review/find.do")
    public ReviewDto find(@RequestParam int reviewno) {
        return reviewService.find(reviewno);
    }

    // 리뷰 정보 수정
    @PutMapping("/review/update.do")
    public boolean update(@RequestBody ReviewDto reviewDto) {
        return reviewService.update(reviewDto);
    }

    // 리뷰 삭제
    @DeleteMapping("/review/delete.do")
    public boolean delete(@RequestParam int reviewno) {
        return reviewService.delete(reviewno);
    }

    // 실세 사용 데이터
    @GetMapping("/review/findbycenter.do")
    public List<ReviewDto> findByCenter(@RequestParam int centerno) {
        return reviewService.findByCenter(centerno);
    }

}
