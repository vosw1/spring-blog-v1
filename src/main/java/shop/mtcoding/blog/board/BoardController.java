package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session; // DI
    private final BoardRepository boardRepository; // DI

    @PostMapping("board/save") //save 주소 만들기
    public String save(BoardRequest.SaveDto requestDTO) { // 정보 담아오기
        System.out.println(requestDTO); // 정보 받아왔는지 확인하기
        return "redirect:/index"; // 인덱스 페이지로 리다이렉션시키기
    }

    // http://localhost:8080?page=0
    @GetMapping({"/", "/board"})
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);
        return "index";
    }

    @GetMapping("/board/saveForm") // /board/saveForm Get요청이 옴
    public String saveForm() { // session 영역에 접근하기 위한
        // 1. session 영역에 sessionUser 키 값에 user 객체가 있는지 체크하기
        User sessionUser = (User) session.getAttribute("sessionUser");
        // 2. 값이 null이면 로그인 페이지로 리다이렉션
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 3. null이 아니면 /board/saveForm으로 이동
        return "board/saveForm";
    }

    // 상세보기시 호출
    @GetMapping("/board/{id}") // 1이 프라이머리키 -> 뭐든 넣어도 실행시키려면 변수화시켜서 {}
    public String detail(@PathVariable int id) {
        System.out.println("id : " + id);
        return "board/detail";
    }
}