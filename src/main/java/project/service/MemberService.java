package project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import project.model.dto.MemberDto;
import project.model.entity.MemberEntity;
import project.model.repository.MemberRepository;

import java.security.SecureRandom;
import java.util.List;


@Service
public class MemberService{
    @Autowired private MemberRepository memberRepository;
//     회원 가입
    @Transactional
    public boolean signup(MemberDto memberDto){
        System.out.println("===== 회원가입 시도 =====");
        System.out.println("이메일: " + memberDto.getMemail());
        System.out.println("이름: " + memberDto.getMname());
        System.out.println("전화번호: " + memberDto.getMphone());

        // 이메일 중복 검사
        MemberEntity existingMember = memberRepository.findByMemail(memberDto.getMemail());
        if(existingMember != null) {
            System.out.println("회원가입 실패: 이미 사용 중인 이메일");
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 유효성 검사 추가
        if(memberDto.getMpwd() == null || memberDto.getMpwd().isEmpty()) {
            System.out.println("회원가입 실패: 비밀번호 누락");
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }

        // 비밀번호 암호화
//        memberDto.setMpwd(passwordEncoder.encode(memberDto.getMpwd()));

        MemberEntity memberEntity = memberDto.toEntity();
        MemberEntity saveEntity = memberRepository.save(memberEntity);

        boolean result = saveEntity.getMno() > 0;
        System.out.println("회원가입 결과: " + (result ? "성공" : "실패"));
        System.out.println("===== 회원가입 완료 =====");
        return result;
    }

//    // 시큐리티에서의 로그인 함수
//    @Override
//    public UserDetails loadUserByUsername(String memail) throws UsernameNotFoundException{
//        System.out.println("===== 로그인 시도 =====");
//        System.out.println("입력된 이메일: " + memail);
//
//        try {
//            // 입력받은 memail 이용하여 데이터베이스의 저장된 암호화패스워드 가져오기
//            MemberEntity memberEntity = memberRepository.findByMemail(memail);
//            if(memberEntity == null) {
//                System.out.println("로그인 실패: 존재하지 않는 이메일");
//                throw new UsernameNotFoundException("이메일이 존재하지 않습니다: " + memail);
//            }
//
//            // 입력받은 이메일의 엔티티가 존재하면 암호화된 패스워드 확인
//            String password = memberEntity.getMpwd();
//            System.out.println("데이터베이스 저장 암호화 비밀번호 길이: " + password.length());
//            System.out.println("암호화 형식 확인: " + (password.startsWith("$2a$") ? "BCrypt 형식 맞음" : "BCrypt 형식 아님"));
//
//            // 입력받은 email과 입력받은 email의 암호화된 패스워드 리턴
//            UserDetails user = User.builder()
//                    .username(memail)
//                    .password(password)
//                    .roles("USER") // 기본 역할 추가
//                    .build();
//
//            System.out.println("사용자 정보 로드 완료");
//            System.out.println("===== 로그인 프로세스 완료 =====");
//            return user;
//        } catch (Exception e) {
//            System.out.println("로그인 처리 중 예외 발생: " + e.getMessage());
//            e.printStackTrace();
//            throw new UsernameNotFoundException("로그인 처리 중 오류가 발생했습니다", e);
//        }
//    }

    // 로그인 함수
    @Transactional
    public boolean login(@RequestBody MemberDto memberDto) {
        boolean result = memberRepository.existsByMemailAndMpwd(memberDto.getMemail(), memberDto.getMpwd());
        if(result == true) {
            System.out.println("로그인 성공");
            setSesseion(memberDto.getMemail());
            return true;
        } else {
            System.out.println("로그인 실패");
            return false;
        }
    }


    // 세션 관련 함수
    @Autowired private HttpServletRequest request;
    // 세션 객체 내 정보 추가
    public boolean setSesseion(String memail) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("loginId", memail);
        return true;
    }

//    // 세션객체 내 정보 반환
//    public String getSession(){
//        System.out.println("===== 세션 정보 조회 =====");
//        try {
//            // 시큐리티에서 자동으로 생성한 로그인 세션 꺼내기
//            Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            // 만약 비로그인이면 null 반환
//            if(object.equals("anonymousUser")){
//                System.out.println("비로그인 상태");
//                return null;
//            }
//            // 로그인 상태이면 로그인 구현시 loadUserByUsername 메소드에서 반환한 userDetails로 타입 변환
//            UserDetails userDetails = (UserDetails) object;
//            // 로그인 정보에서 memail 꺼냄
//            String loginMemail = userDetails.getUsername(); // username == memail
//            System.out.println("로그인된 이메일: " + loginMemail);
//            System.out.println("===== 세션 정보 조회 완료 =====");
//            // 로그인된 email 반환
//            return loginMemail;
//        } catch (Exception e) {
//            System.out.println("세션 정보 조회 중 예외 발생: " + e.getMessage());
//            System.out.println("===== 세션 정보 조회 실패 =====");
//            return null;
//        }
//    }

    // 세션객체 내 정보 반환
    public String getSession(){
        System.out.println("===== 세션 정보 조회 =====");
        HttpSession httpSession = request.getSession();
        Object object = httpSession.getAttribute("loginId");
        if(object != null) {
            String memail = (String)object;
            return memail;
        }
        return null;
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
            if(memberEntity == null) {
                System.out.println("회원 정보 조회 실패: 회원 정보 없음");
                return null;
            }
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
            if(memberEntity == null) {
                System.out.println("회원 탈퇴 실패: 회원 정보 없음");
                return false;
            }
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

//    // 현재 로그인된 회원 정보 수정
//    @Transactional
//    public boolean myUpdate(MemberDto memberDto) {
//        System.out.println("===== 회원 정보 수정 시도 =====");
//        String memail = getSession();
//        if(memail != null) {
//            MemberEntity memberEntity = memberRepository.findByMemail(memail);
//            if(memberEntity == null) {
//                System.out.println("회원 정보 수정 실패: 회원 정보 없음");
//                return false;
//            }
//
//            // 수정 전 정보
//            System.out.println("수정 전 회원 정보:");
//            System.out.println("이름: " + memberEntity.getMname());
//            System.out.println("전화번호: " + memberEntity.getMphone());
//
//            // 이름과 전화번호만 업데이트 (이메일은 변경 불가)
//            if(memberDto.getMname() != null && !memberDto.getMname().isEmpty()) {
//                memberEntity.setMname(memberDto.getMname());
//                System.out.println("이름 수정: " + memberDto.getMname());
//            }
//
//            if(memberDto.getMphone() != null && !memberDto.getMphone().isEmpty()) {
//                memberEntity.setMphone(memberDto.getMphone());
//                System.out.println("전화번호 수정: " + memberDto.getMphone());
//            }
//
//            // 비밀번호 변경 처리 (입력된 경우에만)
//            if(memberDto.getMpwd() != null && !memberDto.getMpwd().isEmpty()) {
//                // 암호화된 비밀번호 저장
//                String encodedPassword = passwordEncoder.encode(memberDto.getMpwd());
//                memberEntity.setMpwd(encodedPassword);
//                System.out.println("비밀번호 수정 완료");
//            }
//
//            // 변경 내용 저장
//            memberRepository.save(memberEntity);
//
//            System.out.println("회원 정보 수정 성공");
//            System.out.println("===== 회원 정보 수정 완료 =====");
//            return true;
//        }
//        System.out.println("회원 정보 수정 실패: 로그인 상태 아님");
//        System.out.println("===== 회원 정보 수정 완료 =====");
//        return false;
//    }
    @Transactional
    public boolean myUpdate(@RequestBody MemberDto memberDto) {
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);
            memberEntity.setMname(memberDto.getMname());
            memberEntity.setMphone(memberDto.getMphone());
            memberEntity.setMpwd(memberDto.getMpwd());
            return true;
        }
        return false;
    }

    /*
     * 비밀번호 찾기 (임시 비밀번호 생성 및 이메일 전송)
     * @param memail 사용자 이메일
     * @return 비밀번호 재설정 성공 여부
     */
    @Transactional
    public boolean resetPassword(String memail) {
        System.out.println("===== 비밀번호 찾기 시도 =====");
        System.out.println("이메일: " + memail);

        // 회원 정보 확인
        MemberEntity memberEntity = memberRepository.findByMemail(memail);
        if (memberEntity == null) {
            System.out.println("비밀번호 찾기 실패: 존재하지 않는 이메일");
            return false;
        }

        // 임시 비밀번호 생성
        String temporaryPassword = generateTemporaryPassword();
        System.out.println("생성된 임시 비밀번호: " + temporaryPassword);

        // 임시 비밀번호 저장
        memberEntity.setMpwd(temporaryPassword);
        memberRepository.save(memberEntity);

        // 실제 서비스에서는 이메일 전송 로직이 필요합니다.
        // 이 예제에서는 콘솔에 출력만 합니다.
        System.out.println("임시 비밀번호 이메일 전송 (메일 서비스 연동 필요): " + memail);

        System.out.println("비밀번호 찾기 성공");
        System.out.println("===== 비밀번호 찾기 완료 =====");
        return true;
    }

    /**
     * 임시 비밀번호 생성
     * @return 생성된 임시 비밀번호
     */
    private String generateTemporaryPassword() {
        // 임시 비밀번호에 사용할 문자셋
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

        // 임시 비밀번호 길이
        int length = 10;

        // 보안 난수 생성기
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}