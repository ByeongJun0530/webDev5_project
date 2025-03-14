package project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.service.MemberService;

@Configuration
public class SecurityConfig {
    // 시큐리티 필터 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // HTTP URL 요청에 따른 부여된 권한/상태 확인 후 자원 허가 제한
        http.authorizeHttpRequests((httpReq) -> {
            httpReq
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/chat")).hasRole("USER") // 임시
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/admin")).hasAnyRole("admin") // 관리자ㅏ 접근
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll();
        });
        http.csrf(AbstractHttpConfigurer :: disable); // csrf 끄기 (개발시에만)

        // [5] 로그인 , 시큐리티에 로그인 기능 [커스텀] 을 제공 JSON 형식 아닌 form 형식으로 지원
        // 로그인 , 시큐리티에 로그인 기능 [커스텀] 을 제공 - JSON 형식으로 통일
        http.formLogin( loginForm -> loginForm
                .loginPage("/member/login") // 로그인을 할 view page url 정의
                .loginProcessingUrl("/member/login.do") // 로그인을 처리할 요청 URL 정의 // POST 방식
                .usernameParameter("memail")   // 로그인에 사용할 id 변수명
                .passwordParameter("mpwd")  // 로그인에 사용한 password 변수명
                // JSON 형식 응답으로 통일
                .successHandler( ( request , response , exception ) -> {
                    System.out.println("로그인 성공!!!");
                    response.setContentType("application/json");
                    response.getWriter().println("{\"success\":true}");
                })
                .failureHandler( ((request, response, exception) -> {
                    System.out.println("로그인 실패!!!");
                    response.setContentType("application/json");
                    response.getWriter().println("{\"success\":false}");
                }))
        );

        // 로그 아웃
        http.logout(logout -> logout
                .logoutUrl("/member/logout.do") // 로그아웃 처리 요청 url
                .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 page
                .invalidateHttpSession(true) //로그아웃 성공시 세션 초기화
        );

        // 로그인 처리 서비스 객체 정의
        http.userDetailsService(  memberService );

        // 객체 빌드/실행하여 보안 필터 체인 생성
        return http.build();
    }
    @Autowired MemberService memberService;

    // 암호화
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
