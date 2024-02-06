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

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        System.out.println("sessionUser:" + sessionUser);
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        // 3. 모델 위임
        // insert into board_tb(title, content, user_id, created_at) values(?,?,?, now());
        boardRepository.save(requestDTO, sessionUser.getId());

        return "redirect:/";
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
    public String detail(@PathVariable int id, HttpServletRequest request) {
        System.out.println("id : " + id);
        // 1. 바로 모델 진입 -> 상세보기 데이터 가져오기
        // body 데이터가 없으면 유효성 검사할 필요 없음
        BoardResponse.DetailDTO reponseDTO = boardRepository.findById(id);

        // user 객체를 가져와서 session 값 받기 : object라 다운 캐스팅 해야함
        User sessionUser = (User) session.getAttribute("sessionUser");
        //System.out.println("sessionUser: " + sessionUser);

        // 2. 페이지 주인 여부 체크(board의 userId와 sessionId의 값 비교)
        boolean pageOwner = false;

        if (reponseDTO.getUserId() == sessionUser.getId()) {
            //System.out.println("getUserId:" + reponseDTO.getUserId());
            pageOwner = true;
        }

        request.setAttribute("board", reponseDTO);
        request.setAttribute("pageOwner", pageOwner); // 이 값을 mustache에게 줘야함!

        return "board/detail";
    }
}