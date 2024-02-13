package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration// 메모리에 띄우기 위한 문법
public class SecurityConfig {

    @Bean // 다른걸로 hash할 경우 변경해야 함
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    } // 패스워드 인코더 디폴트 설정

    // 인증과 상관없이 열어야 하는 주소
    // 주소 설계를 잘해야 함
    @Bean // IoC에 뜸
    public WebSecurityCustomizer ignore(){ // 정적 파일만 security filter에서 제외시키기
        return w -> w.ignoring().requestMatchers("/static/**", "/h2-console/**");
    }

    @Bean // IoC에 뜸
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(c->c.disable());

        // 주소로 필터링 : 인증이 필요한 페이지를 주소로 구분
        http.authorizeHttpRequests(a -> {
            a.requestMatchers(RegexRequestMatcher.regexMatcher("/board/\\d+")).permitAll()
                    .requestMatchers("/user/**", "/board/**").authenticated() // 인증이 필요한 페이지
                    .anyRequest().permitAll(); // 인증이 필요없는 페이지
        });

        // 기본 주소를 우리가 만든 페이지로 변경함
        http.formLogin(f -> {
            // 시큐리티가 들고있는 페이지를 사용할 것
            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/")
                    .failureUrl("/loginForm"); // 실패하면 이동
        });

        return http.build(); // 코드의 변경이 없으면 부모 이름(추상적)으로 리턴할 필요 없음
    }
}
