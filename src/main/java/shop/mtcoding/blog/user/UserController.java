package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRequest;

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

    @GetMapping("/loginForm") // view만 원함
    public String loginForm() {
        return "user/loginForm";
    }

    // 원래는 get요청이나 예외 post요청하면 됨
    // 민감한 정보는 쿼리 스트링에 담아보낼 수 없음
    //원래는 get요청이나 예외 post요청하면 됨
    //민감한 정보는 쿼리 스트링에 담아보낼 수 없음
//    @PostMapping("/login")
//    public String login(UserRequest.LoginDTO requestDTO) {
//
//        // 1. 유효성 검사
//        if (requestDTO.getUsername().length() < 3) {
//            return "error/400";
//        }
//
//        // 2. 모델 필요 select * from user_tb where username=? and password=?
//        User user = userRepository.findByUsernameAndPassword(requestDTO); // DB에 조회할때 필요하니까 데이터를 받음
//        if (user == null) {
//            return "error/401";
//        } else {
//            session.setAttribute("sessionUser", user);
//            return "redirect:/";
//        }
//    }

    @GetMapping("/joinForm") // view만 원함
    public String joinForm() {
        return "user/joinForm";
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

    @GetMapping("/user/updateForm")
    public String updateForm() {
//        // 인증 체크하기
//        User sessionUser = (User) session.getAttribute("sessionUser");
//        if (sessionUser == null) {
//            return "redirect:/loginForm";
//        }
        return "/user/updateForm";
    }

    @PostMapping("/user/update")
    public String updateUser(UserRequest.UpdateDTO requestDTO, HttpServletRequest request) {
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
    public String logout() {
        // 1번 서랍에 있는 uset를 삭제해야 로그아웃이 됨
        session.invalidate(); // 서랍의 내용 삭제
        return "redirect:/";
    }
}