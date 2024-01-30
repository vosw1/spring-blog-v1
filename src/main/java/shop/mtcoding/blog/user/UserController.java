package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/*컨트롤러
1. 요청받기 (URL(LOCATION) - URI(IDENTIFY) 포함)
2. 데이터(body)는 DTO로 받음
3. 기본 mime 전략 - 데이터타입 x.www.form.urlencoded (키=값&키=값)
4. 유효성 검사하기(body 데이터가 있으면)
4. 클라이언트가 View만 원하는지? 혹은 DB처리후 view도 원하는지?
5. View만 원하면 View만 응답하면 끝
7. DB처리를 원하면 모델에게 위임(DAO:서비스)한 후 View를 응답하면 끝
* */

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository; // null
    private final HttpSession session;

    //원래는 get요청이나 예외 post요청하면 됨
    @PostMapping("/login")
    public String login(UserRequest.loginDTO requestDTO) { // 민감한 정보는 쿼리 스트링에 담아보낼 수 없음
        // 1. 유효성 검사
        if(requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 2. 모델 필요 select * from user_tb where username=? and password=?
        User user = userRepository.findByUsernameAndPassword(requestDTO); // DB에 조회할때 필요하니까 데이터를 받음

        if (user == null) {
            return "error/401";
        }else {
            session.setAttribute("sessionUser", user);
            return "redirect:/";
        }

    }

    @PostMapping("/join")
    public String join(UserRequest.joinDTO requestDTO){
        System.out.println(requestDTO);

        // 1. 유효성 검사
        if(requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        //2. 모델에게 위임하기
        userRepository.save(requestDTO);
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm") // view만 원함
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm") // view만 원함
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @CreationTimestamp
    private LocalDateTime CreatedAt; // insert시 날짜 자동 생성
}