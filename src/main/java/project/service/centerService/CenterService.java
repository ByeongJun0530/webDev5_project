package project.service.centerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.dto.centerDto.CenterDto;
import project.model.entity.centerEntity.CenterEntity;
import project.model.repository.centerRepository.CenterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CenterService {
    @Autowired
    private CenterRepository centerRepository;

    // 센터 등록
    public boolean upload(CenterDto centerDto) {
        CenterEntity centerEntity = centerDto.toEntity();
        // 파일로 저장할떄 여기 사진 저장 코드 추가
        centerRepository.save(centerEntity);
        return true;
    }

    // 센터 전체 조회
    public List<CenterDto> findall() {
        List<CenterEntity> centers = centerRepository.findAll();
        return centers.stream().map(CenterEntity::toDto).collect(Collectors.toList());
        //페이징 처리 추가 & 카테고리 나누면 처리
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
            // centerEntity.setPhoto(photoPath); // 파일 저장 경로 설정 필요
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
