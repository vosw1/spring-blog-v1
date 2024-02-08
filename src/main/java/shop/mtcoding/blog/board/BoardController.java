package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        // 게시글 목록 조회하기
        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList); // ("key", value)
        return "index";
    }

    @GetMapping("/board/saveForm") // 글쓰기 화면
    public String saveForm() {
        // 익명이라 인증, 권한 체크가 필요 없음
        // 바로 이동하면 됨
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm") // 글 수정 화면
    public String updateForm(@PathVariable int id) {

        return "board/updateForm";
    }

    @PostMapping("/board/save") // 글쓰기 저장
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request){
        // 익명이라 인증 필요 없음
        // 1. 유효성 검사하기
        if (requestDTO.getTitle().length() > 20 && requestDTO.getContent().length() > 20) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title 또는 content의 길이가 20자를 초과해서는 안됩니다.");
            return "error/400"; // 400 Bad Request 오류 페이지로 이동
        }

        // 2. 모델 위임
        boardRepository.save(requestDTO);

        // 3. 확인하기
        System.out.println("저장된 게시물: " + requestDTO);
        return "redirect:/";
    }

    @PostMapping("/board/{id}/update") // 수정하기
    public String update(@PathVariable int id){
        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete") // 삭제하기
    public String delete(@PathVariable int id){
        // 해당하는 id의 게시물을 삭제
        boardRepository.deleteById(id);

        return "redirect:/";
    }
}
