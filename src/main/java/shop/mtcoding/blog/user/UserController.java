package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class UserController {

    // fianl 변수는 반드시 초기화 되어야 함
    private final UserRepository userRepository; // null
    private final HttpSession session;

    // @AllArgsConstructor를 사용하면서 필요 없어짐
//    public UserController(UserRepository userRepository, HttpSession session) {
//        this.userRepository = userRepository;
//        this.session = session;
//    }

    // 원래는 get요청이나 예외 post요청하면 됨
    // 민감한 정보는 쿼리 스트링에 담아보낼 수 없음
    //원래는 get요청이나 예외 post요청하면 됨
    //민감한 정보는 쿼리 스트링에 담아보낼 수 없음
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {

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
    public String join(UserRequest.JoinDTO requestDTO) {
        System.out.println(requestDTO);

        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }
        userRepository.save(requestDTO); // 모델에 위임하기
        return "redirect:/loginForm"; //리다이렉션불러놓은게 있어서 다시부른거
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
}