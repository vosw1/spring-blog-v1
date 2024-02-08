package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.Constant;
import shop.mtcoding.blog._core.PagingUtil;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping({ "/", "/board"})
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        // 게시글 목록 조회
        List<Board> boardList = boardRepository.findPage(page);
        request.setAttribute("boardList", boardList); // 가방에 담음

        // 현재 페이지
        int currentPage = page;
        int nextPage = currentPage+1;
        int prevPage = currentPage-1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);
        // 이것만 담으면 디세이브를 못한다.

        // 현재 페이지가 퍼스트인지 라스트인지 만든다.
        boolean first = currentPage == 0 ? true : false;

        int boardTotalCount = boardRepository.findBoardTotalCount();
        System.out.println(boardTotalCount);
        int pagingCount = 3;
        int totalPageCount = boardTotalCount/pagingCount;

        boolean last = currentPage == totalPageCount-2? true : false;

        request.setAttribute("first", first);
        request.setAttribute("last", last);


        int endPage = totalPageCount-2;
        List<Integer> pageNumbers = new ArrayList<>();

        // 시작 페이지부터 끝 페이지까지 페이지 번호를 추가
        for (int i = 0; i <= endPage; i++) {
            pageNumbers.add(i);
        }

        System.out.println(pageNumbers);
        request.setAttribute("pageNumbers", pageNumbers);

        return "index";
    }

    @GetMapping("/board/saveForm") // 글쓰기 화면
    public String saveForm() {
        // 익명이라 인증, 권한 체크가 필요 없음
        // 바로 이동하면 됨
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm") // 글 수정 화면
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        // 해당하는 ID의 게시물을 가져와서 요청에 속성으로 추가
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
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
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO) {

        // 업데이트 메서드 실행
        boardRepository.updateById(requestDTO, id);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete") // 삭제하기
    public String delete(@PathVariable int id){
        // 해당하는 id의 게시물을 삭제
        boardRepository.deleteById(id);

        return "redirect:/";
    }
}
