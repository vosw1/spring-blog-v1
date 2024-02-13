package shop.mtcoding.blog._core.config.security;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;

/*
* 조건
* post 요청
* "/login"요청
* x-www-form-urlencoded
* 키값이 username, password*/

@AllArgsConstructor
@Service
public class MyLoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private  final HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername: " + username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("user는 null");
            return null; // 로그인 진행하던걸 취소하고 알아서 응답해줌 -> 반환할 페이지를 알려줘야함
        } else {
            System.out.println("user를 찾았어요");
            session.setAttribute ("sessionUser", user); // 머스태치에서만 가져오기
            return new MyLoginUser(user); // securityContextHolder에 저장됨
        }
    }
}
