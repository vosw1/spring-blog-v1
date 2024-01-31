package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/*
 * 컨트롤러
 * 1. 요청받기 (URL - URI 포함 )
 * 2. http body는 어떻게? (DTO)
 * 3. 기본 MIME 전략 : x-www-form-urlencoded (username=ssar&password=1234)
 * 4. 유효성 검사하기 (body 데이터가 있다면)
 * 5. 클라이언트가 View(Mustache)만 원하는 지? 혹은  DB 처리 후 View도 원하는지???
 * 6. View만 원하면 View를 응답하면 끈ㅌ
 * 7. DB처리를 원하면 Model(DAO)에게 위임 후 view를 응답하면 끝
 */


@Controller
public class UserController {
    private final UserRepository userRepository;
    private final HttpSession session;

    public UserController(UserRepository userRepository, HttpSession session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    @PostMapping("/login")
    public String login(UserRequest.loginDTO requestDTO) {
        //1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        //2. 모델 필요 select * from user_tb where username= ? and password = ?
        User user = userRepository.findByUsernameAndPassword(requestDTO);

        if (user == null) {
            return "error/401";
        } else {
            session.setAttribute("sessionUser", user);
            return "redirect:/";
        }


        //3. 응답
    }

    @PostMapping("/join")
    public String join(UserRequest.joinDTO requestDTO) {
        System.out.println(requestDTO);
        //1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }
        //2. 동일 username 체크 (나중에 하나의 트랜잭션으로 묶는 것이 좋다. )
        User user = userRepository.findByUsername(requestDTO.getUsername());
        if (user == null ){
            //3. Model에게 위임하기
            userRepository.save(requestDTO);
        } else {
            return "error/400";
        }
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {

        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}