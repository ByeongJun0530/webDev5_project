package project.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.MemberEntity;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    // 로그인 추상메소드
    boolean existsByMemailAndMpwd(String memail, String mpwd);
    // 아이디로 엔티티 조회
     MemberEntity findByMemail(String memail);
}