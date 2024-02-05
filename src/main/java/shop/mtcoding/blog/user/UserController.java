package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    // fianl 변수는 반드시 초기화 되어야 함
    private final UserRepository userRepository; // null
    private final HttpSession session;

    public UserController(UserRepository userRepository, HttpSession session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    // 원래는 get요청이나 예외 post요청하면 됨
    // 민감한 정보는 쿼리 스트링에 담아보낼 수 없음
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO){

        System.out.println(requestDTO); // toString -> @Data

        if(requestDTO.getUsername().length() < 3){
            return "error/400"; // ViewResolver 설정이 되어 있음. (앞 경로, 뒤 경로)
        }

        User user = userRepository.findByUsernameAndPassword(requestDTO);

        if(user == null){ // 조회 안됨 (401)
            return "error/401";
        }else{ // 조회 됐음 (인증됨)
            session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)
        }

        return "redirect:/"; // 컨트롤러가 존재하면 무조건 redirect 외우기
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