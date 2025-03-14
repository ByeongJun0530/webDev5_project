package project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.model.dto.MemberDto;
import project.model.entity.MemberEntity;
import project.model.repository.MemberRepository;


@Service
public class MemberService implements UserDetailsService {
    @Autowired private MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public boolean signup(MemberDto memberDto){
        System.out.println("===== 회원가입 시도 =====");
        System.out.println("이메일: " + memberDto.getMemail());
        System.out.println("이름: " + memberDto.getMname());
        System.out.println("전화번호: " + memberDto.getMphone());

        // 비밀번호 유효성 검사 추가
        if(memberDto.getMpwd() == null || memberDto.getMpwd().isEmpty()) {
            System.out.println("회원가입 실패: 비밀번호 누락");
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }

        MemberEntity memberEntity = memberDto.toEntity();
        MemberEntity saveEntity = memberRepository.save(memberEntity);

        boolean result = saveEntity.getMno() > 0;
        System.out.println("회원가입 결과: " + (result ? "성공" : "실패"));
        System.out.println("===== 회원가입 완료 =====");
        return result;
    }

    // 시큐리티에서의 로그인 함수
    @Override
    public UserDetails loadUserByUsername(String memail) throws UsernameNotFoundException{
        System.out.println("===== 로그인 시도 =====");
        System.out.println("입력된 이메일: " + memail);

        // 입력받은 memail 이용하여 데이터베이스의 저장된 암호화패스워드 가져오기
        MemberEntity memberEntity = memberRepository.findByMemail(memail); // 입력받은 이메일로 회원엔티티 찾기
        if(memberEntity == null) {
            System.out.println("로그인 실패: 존재하지 않는 이메일");
            throw new UsernameNotFoundException("없는 아이디입니다");
        }

        // 입력받은 이메일의 엔티티가 존재하면 암호화된 패스워드 확인
        String password = memberEntity.getMpwd();
        System.out.println("데이터베이스 저장 암호화 비밀번호: " + password);

        // 입력받은 email과 입력받은 email의 암호화된 패스워드 리턴
        UserDetails user = User.builder()
                .username(memail)
                .password(password)
                .roles("USER") // 기본 역할 추가
                .build();

        System.out.println("사용자 정보 로드 완료");
        System.out.println("===== 로그인 프로세스 완료 =====");
        // userDetails 반환
        return user;
    }

    // 세션 관련 함수
    @Autowired private HttpServletRequest request;
    // 세션객체 내 정보 반환
    public String getSession(){
        System.out.println("===== 세션 정보 조회 =====");
        // 시큐리티에서 자동으로 생성한 로그인 세션 꺼내기
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 만약 비로그인이면 null 반환
        if(object.equals("anonymousUser")){
            System.out.println("비로그인 상태");
            return null;
        }
        // 로그인 상태이면 로그인 구현시 loadUserByUsername 메소드에서 반환한 userDetails로 타입 변환
        UserDetails userDetails = (UserDetails) object;
        // 로그인 정보에서 memail 꺼냄
        String loginMemail = userDetails.getUsername(); // username == memail
        System.out.println("로그인된 이메일: " + loginMemail);
        System.out.println("===== 세션 정보 조회 완료 =====");
        // 로그인된 email 반환
        return loginMemail;
    }

    // 세션 객체내 정보 초기화
    public boolean deleteSession(){
        System.out.println("===== 세션 정보 삭제 =====");
        HttpSession httpSession = request.getSession();
        // 객체 안 특정한 속성명 제거
        httpSession.removeAttribute("loginId");
        System.out.println("세션 정보 삭제 완료");
        System.out.println("===== 세션 정보 삭제 완료 =====");
        return true;
    }

    // 현재 로그인된 회원의 회원정보 조회
    public MemberDto getMyInfo(){
        System.out.println("===== 회원 정보 조회 =====");
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);
            MemberDto memberDto = memberEntity.toDto();
            System.out.println("회원 정보: " + memberDto.toString());
            System.out.println("===== 회원 정보 조회 완료 =====");
            return memberDto;
        }
        System.out.println("회원 정보 조회 실패: 로그인 상태 아님");
        System.out.println("===== 회원 정보 조회 완료 =====");
        return null;
    }

    // 현재 로그인된 회원 탈퇴
    public boolean myDelete() {
        System.out.println("===== 회원 탈퇴 시도 =====");
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);
            System.out.println("탈퇴할 회원 이메일: " + memail);
            memberRepository.delete(memberEntity);
            deleteSession();
            System.out.println("회원 탈퇴 성공");
            System.out.println("===== 회원 탈퇴 완료 =====");
            return true;
        }
        System.out.println("회원 탈퇴 실패: 로그인 상태 아님");
        System.out.println("===== 회원 탈퇴 완료 =====");
        return false;
    }

    // 현재 로그인된 회원 정보 수정
    @Transactional
    public boolean myUpdate(MemberDto memberDto) {
        System.out.println("===== 회원 정보 수정 시도 =====");
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);

            // 수정 전 정보
            System.out.println("수정 전 회원 정보:");
            System.out.println("이름: " + memberEntity.getMname());
            System.out.println("전화번호: " + memberEntity.getMphone());

            // 이름과 전화번호만 업데이트 (이메일은 변경 불가)
            if(memberDto.getMname() != null && !memberDto.getMname().isEmpty()) {
                memberEntity.setMname(memberDto.getMname());
                System.out.println("이름 수정: " + memberDto.getMname());
            }

            if(memberDto.getMphone() != null && !memberDto.getMphone().isEmpty()) {
                memberEntity.setMphone(memberDto.getMphone());
                System.out.println("전화번호 수정: " + memberDto.getMphone());
            }

            // 비밀번호 변경 처리 (입력된 경우에만)
            if(memberDto.getMpwd() != null && !memberDto.getMpwd().isEmpty()) {
                String encodedPassword = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(memberDto.getMpwd());
                memberEntity.setMpwd(encodedPassword);
                System.out.println("비밀번호 수정 완료");
            }

            System.out.println("회원 정보 수정 성공");
            System.out.println("===== 회원 정보 수정 완료 =====");
            return true;
        }
        System.out.println("회원 정보 수정 실패: 로그인 상태 아님");
        System.out.println("===== 회원 정보 수정 완료 =====");
        return false;
    }
}