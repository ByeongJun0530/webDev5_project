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
        // 비밀번호 유효성 검사 추가
        if(memberDto.getMpwd() == null || memberDto.getMpwd().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
        MemberEntity memberEntity = memberDto.toEntity();
        MemberEntity saveEntity = memberRepository.save(memberEntity);
        return saveEntity.getMno() > 0;
    }

    // 시큐리티에서의 로그인 함수
    @Override
    public UserDetails loadUserByUsername(String memail) throws UsernameNotFoundException{
        System.out.println("MemberService.loadUserByUsername");
        System.out.println("memail = " + memail);// 로그인 시 입력받은 email

        // 입력받은 memail 이용하여 데이터베이스의 저장된 암호화패스워드 가져오기
        MemberEntity memberEntity = memberRepository.findByMemail(memail); // 입력받은 이메일로 회원엔티티 찾기
        if(memberEntity == null) {
            // 입력받은 이메일의 엔티티가 없으면 예외 클래스 강제 실행
            throw new UsernameNotFoundException("없는 아이디입니다");
        }
        // 입력받은 이메일의 엔티티가 존재하면 암호화된 패스워드 확인
        String password = memberEntity.getMpwd();
        System.out.println("password = " + password);

        // 입력받은 email과 입력받은 email의 암호화된 패스워드 리턴
        UserDetails user = User.builder()
                .username(memail)
                .password(password)
                .build();

        // userDetails 반환
        return user;
    }

    // 세션 관련 함수
    @Autowired private HttpServletRequest request;
    // 세션객체 내 정보 반환
    public String getSession(){
        // 시큐리티에서 자동으로 생성한 로그인 세션 꺼내기
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 만약 비로그인이면 null 반환
        if(object.equals("anonymousUser")){return null;}
        // 로그인 상태이면 로그인 구현시 loadUserByUsername 메소드에서 반환한 userDetails로 타입 변환
        UserDetails userDetails = (UserDetails) object;
        // 로그인 정보에서 memail 꺼냄
        String loginMemail = userDetails.getUsername(); // username == memail
        // 로그인된 email 반환
        return loginMemail;
    }

    // 세션 객체내 정보 초기화
    public boolean deleteSession(){
        HttpSession httpSession = request.getSession();
        // 객ㅊ체 안 특정한 속성명 제거
        httpSession.removeAttribute("loginId");
        return true;
    }

    // 현재 로그인된 회원의 회원정보 조회
    public MemberDto getMyInfo(){
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);
            MemberDto memberDto = memberEntity.toDto();
            return memberDto;
        }
        return null;
    }

    // 현재 로그인된 회원 탈퇴
    public boolean myDelete() {
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);
            memberRepository.delete(memberEntity);
            deleteSession();
            return true;
        }
        return false;
    }

    // 현재 로그인된 회원 정보 수정, mname, phone
    @Transactional
    public boolean myUpdate(MemberDto memberDto) {
        String memail = getSession();
        if(memail != null) {
            MemberEntity memberEntity = memberRepository.findByMemail(memail);
            memberEntity.setMname(memberDto.getMname());
            memberEntity.setMphone(memberDto.getMphone());
            return true;
        }
        return false;
    }
}
