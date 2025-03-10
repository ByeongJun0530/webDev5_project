package project.controller.centerController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.model.dto.centerDto.CenterDto;
import project.service.centerService.CenterService;

import java.util.List;

@RestController
public class CenterController {
    @Autowired
    private CenterService centerService;

    // 센터 등록
    @PostMapping("/center/upload.do")
    public boolean upload(@ModelAttribute CenterDto centerDto) {
        return centerService.upload(centerDto);
    }

    // 센터 전체 조회
    @GetMapping("/center/findall.do")
    public List<CenterDto> findall() {
        return centerService.findall();
    }

    // 개별 센터 조회
    @GetMapping("/center/find.do")
    public CenterDto find(@RequestParam int centerno) {
        return centerService.find(centerno);
    }

    // 센터 정보 수정
    @PutMapping("/center/update.do")
    public boolean update(@ModelAttribute CenterDto centerDto) {
        return centerService.update(centerDto);
    }

    // 센터 삭제
    @DeleteMapping("/center/delete.do")
    public boolean delete(@RequestParam int centerno) {
        return centerService.delete(centerno);
    }
}

