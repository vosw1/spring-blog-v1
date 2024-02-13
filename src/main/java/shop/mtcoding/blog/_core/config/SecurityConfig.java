package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration// 메모리에 띄우기 위한 문법
public class SecurityConfig {

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception{
        return http.build(); // 코드의 변경이 없으면 부모 이름(추상적)으로 리턴할 필요 없음
    }
}
