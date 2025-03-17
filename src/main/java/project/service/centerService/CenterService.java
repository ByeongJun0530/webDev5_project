package project.service.centerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.model.dto.centerDto.CenterDto;
import project.model.entity.centerEntity.CenterEntity;
import project.model.repository.centerRepository.CenterRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CenterService {
    @Autowired
    private CenterRepository centerRepository;

    // 센터 등록
    public boolean upload(CenterDto centerDto) {
        if (!centerDto.getUploadFile().isEmpty()) {
            try {
                // 파일 저장 로직
                String fileName = centerDto.getUploadFile().getOriginalFilename();
                String filePath = "C:\\Users\\admin\\Documents\\java\\webDev5_project\\build\\resources\\main\\static\\img\\" + fileName;
                centerDto.getUploadFile().transferTo(new File(filePath));
                centerDto.setPhoto(filePath); // 파일 경로를 DTO에 설정
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        CenterEntity centerEntity = centerDto.toEntity();
        centerRepository.save(centerEntity);
        return true;
    }
//    public boolean upload(CenterDto centerDto) {
//        CenterEntity centerEntity = centerDto.toEntity();
//        centerRepository.save(centerEntity);
//        return true;
//    }

    // 센터 전체 조회
    public List<CenterDto> findAll() {
        List<CenterEntity> centers = centerRepository.findAll();
        return centers.stream().map(CenterEntity::toDto).collect(Collectors.toList());
    }

    // 개별 센터 조회
    public CenterDto find(int centerno) {
        CenterEntity center = centerRepository.findById(centerno).orElse(null);
        if (center != null) {
            return center.toDto();
        }
        return null;
    }

    // 센터 정보 수정
    public boolean update(CenterDto centerDto) {
        CenterEntity centerEntity = centerRepository.findById(centerDto.getCenterno()).orElse(null);
        if (centerEntity != null) {
            CenterEntity updatedCenter = centerDto.toEntity();
            updatedCenter.setCenterno(centerDto.getCenterno());
            centerRepository.save(updatedCenter);
            return true;
        }
        return false;
    }

    // 센터 삭제
    public boolean delete(int centerno) {
        CenterEntity centerEntity = centerRepository.findById(centerno).orElse(null);
        if (centerEntity != null) {
            centerRepository.delete(centerEntity);
            return true;
        }
        return false;
    }
}