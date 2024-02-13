package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog._core.config.security.MyLoginUser;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRequest;

@AllArgsConstructor
@Controller
public class UserController {

    // fianl 변수는 반드시 초기화 되어야 함
    private final UserRepository userRepository; // null
    private final HttpSession session;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/loginForm") // view만 원함
    public String loginForm() {
        return "user/loginForm";
    }

    // 왜 조회인데 post임? 민간함 정보는 body로 보낸다.
    // 로그인만 예외로 select인데 post 사용
    // select * from user_tb where username=? and password=?
//    @PostMapping("/login")
//    public String login(UserRequest.LoginDTO requestDTO) {
//        System.out.println(requestDTO); // toString -> @Data
//
//        if (requestDTO.getUsername().length() < 3) {
//            return "error/400"; // ViewResolver 설정이 되어 있음. (앞 경로, 뒤 경로)
//        }
//
//        User user = userRepository.findByUsernameAndPassword(requestDTO);
//
//        if (user == null) { // 조회 안됨 (401)
//            return "error/401";
//        } else { // 조회 됐음 (인증됨)
//            session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)
//        }
//
//        return "redirect:/"; // 컨트롤러가 존재하면 무조건 redirect 외우기
//    }

    @GetMapping("/joinForm") // view만 원함
        public String joinForm () {
            return "user/joinForm";
        }

        @PostMapping("/join")
        public String join (UserRequest.JoinDTO requestDTO){
            System.out.println(requestDTO);

            // 비밀번호를 hash로 만드는 코드
            String rawPassword = requestDTO.getPassword();
            String encPassword = passwordEncoder.encode(rawPassword);
            requestDTO.setPassword(encPassword);

            // 모델에 위임하기
            userRepository.save(requestDTO);
            return "redirect:/loginForm"; //리다이렉션불러놓은게 있어서 다시부른거
        }

        @GetMapping("/user/updateForm")
        public String updateForm (HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser){
            User user = userRepository.findByUsername(myLoginUser.getUsername());
            request.setAttribute("user", user);
            return "user/updateForm";
        }

        @PostMapping("/user/update")
        public String updateUser (UserRequest.UpdateDTO requestDTO, HttpServletRequest request){
            // 세션에서 사용자 정보 가져오기
            User sessionUser = (User) session.getAttribute("sessionUser");
            if (sessionUser == null) {
                return "redirect:/loginForm"; // 로그인 페이지로 리다이렉트
            }

            // 비밀번호 업데이트
            userRepository.userUpdate(requestDTO, sessionUser.getId());

            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/"; // 홈 페이지로 리다이렉트
        }

        @GetMapping("/logout")
        public String logout () {
            // 1번 서랍에 있는 uset를 삭제해야 로그아웃이 됨
            session.invalidate(); // 서랍의 내용 삭제
            return "redirect:/";
        }
    }