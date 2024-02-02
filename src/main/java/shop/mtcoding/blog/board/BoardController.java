package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog._core.PagingUtil;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;

    // http://localhost:8080?page=0
    @GetMapping({"/", "/board"})
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        //System.out.println("페이지: "+page);
        List<Board> boardList = boardRepository.findAll(page);
        request.setAttribute("boardList", boardList); // 가방에 담음

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);
        // 이것만 담으면 디세이브를 못한다.

        // 현재 페이지가 퍼스트인지 라스트인지 만든다.
        boolean first = PagingUtil.isFirst(currentPage);
        boolean last = PagingUtil.isLast(currentPage, 4);

        request.setAttribute("first", first);
        request.setAttribute("last", last);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    // 상세보기시 호출
    @GetMapping("/board/{id}") // 1이 프라이머리키 -> 뭐든 넣어도 실행시키려면 변수화시켜서 {}
    public String detail(@PathVariable int id, HttpServletRequest request) { // 파싱하게 치환해서 알려줌

        BoardResponse.DetailDTO reponseDTO = boardRepository.findById(id);
        request.setAttribute("board", reponseDTO); // 키를 통해 값을 찾음

        // 1. 해당 페이지의 주인 여부
        boolean owner = false; // 목적

        // 2. 작성자 userId 확인하기
        int boardUserId = reponseDTO.getUserId();

        // 3. 로그인 여부 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser != null) { // 로그인 했고
            if (boardUserId == sessionUser.getId()) {
                owner = true;
            }
        }

        request.setAttribute("owner", owner);

        return "board/detail";
    }
}