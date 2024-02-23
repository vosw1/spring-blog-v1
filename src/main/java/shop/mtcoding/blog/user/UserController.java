package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.ApiUtil;

@AllArgsConstructor
@Controller // 파일을 리턴함 -> @ResponseBody로 메세지 자체를 리턴
public class UserController {

    // fianl 변수는 반드시 초기화 되어야 함
    private final UserRepository userRepository; // null
    private final HttpSession session;

    @GetMapping("/api/username-same-check")
    public @ResponseBody ApiUtil<?> usernameSameCheck(String username){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ApiUtil<>(true); // 가입 가능
        } else {
            return new ApiUtil<>(false); // 가입 불가
        }
    }

    // 원래는 get요청이나 예외 post요청하면 됨
    // 민감한 정보는 쿼리 스트링에 담아보낼 수 없음
    //원래는 get요청이나 예외 post요청하면 됨
    //민감한 정보는 쿼리 스트링에 담아보낼 수 없음
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {

        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 2. 모델 필요 select * from user_tb where username=? and password=?
        User user = userRepository.findByUsernameAndPassword(requestDTO); // DB에 조회할때 필요하니까 데이터를 받음
        session.setAttribute("sessionUser", user);
        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO, HttpServletRequest request) {
        String username = requestDTO.getUsername(); // DTO에서 사용자명 추출

        // 중복 체크
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new RuntimeException("아이디가 중복되었어요");
        }

        // 중복이 아니면 회원가입 처리
        try {
            userRepository.save(requestDTO);
        } catch (Exception e) {
            throw new RuntimeException("회원가입에 실패하였습니다.");
        }

        // 회원가입 성공 후 다시 회원가입 폼으로 이동할 때 사용자 이름 전달
        request.setAttribute("username", username);

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
        // 1번 서랍에 있는 uset를 삭제해야 로그아웃이 됨
        session.invalidate(); // 서랍의 내용 삭제
        return "redirect:/";
    }
}