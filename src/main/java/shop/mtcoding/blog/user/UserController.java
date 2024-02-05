package shop.mtcoding.blog.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserRepository userRepository; // null

    public UserController(UserRepository userRepository) { //IOC 컨테이너에서 써치해서 찾아서 넣어줌
        this.userRepository = userRepository;
    } // 생성자를 만들어서 디폴트 생성자를 없애버림

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