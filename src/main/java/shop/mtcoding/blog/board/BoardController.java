package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.love.LoveRepository;
import shop.mtcoding.blog.love.LoveResponse;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session; // DI
    private final BoardRepository boardRepository; // DI
    private final ReplyRepository replyRepository;
    private final LoveRepository loveRepository;

    // ?title=제목1&content=내용1
    // title=제목1&content=내용1
    // 쿼리 스트링과 -x-www-form-urlencoded와 파싱 방법이 동일함
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO) { // 파싱 전략이 json으로 바뀜
        // System.out.println(requestDTO); 정보 받기 확인

        // 인증 체크하기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 권한 체크하기
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }

        // update board_tb set title =?, content =?, where id =?
        boardRepository.update(requestDTO, id);
        return "redirect:/board/" + id; // 수정한 게시글로 돌아가기
    }

    @GetMapping("/board/{id}/updateForm") // 보드에 해당 페이지
    public String updateFormn(@PathVariable int id, HttpServletRequest request) {

        // 인증 체크하기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 권한 체크하기
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            request.setAttribute("status", 403);
            request.setAttribute("msg", "게시글을 수정할 권한이 없습니다");
            return "error/40x"; // 리다이렉트 하면 데이터 사라지니까 하면 안됨
        }
        // 가방에 담기
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete") // body데이터가 없어서 유효성 검사 안해도 됨
    public String delete(@PathVariable int id) {
        // 1. 인증 검사하기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 2. 권한 검사하기
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }
        boardRepository.deleteById(id);

        return "redirect:/";
    }

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

    // http://localhost:8080?page=0 쿼리스트링
    // 스프링에서 쿼리 스트링 받는 방법은 매개 변수에 받기
    @GetMapping({"/"})
    public String index(HttpServletRequest request,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "") String keyword) {
        // isEmpty : 공백, null
        // isBlank : 공백, null, 화이트 space(눈에 안보이는 공백)
        if (keyword.isBlank()) {
            List<Board> boardList = boardRepository.findAll(page);
            // 전체 페이지 개수
            int count = boardRepository.count().intValue();

            int namerge = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;

            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", "");
        } else {
            List<Board> boardList = boardRepository.findAll(page, keyword);
            // 전체 페이지 개수
            int count = boardRepository.count(keyword).intValue();

            int namerge = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;

            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", keyword);
        }
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
    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

        List<BoardResponse.ReplyDTO> replyDTOList = replyRepository.findByBoardId(id, sessionUser);
        request.setAttribute("board", boardDTO);
        request.setAttribute("replyList", replyDTOList);

        LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id, sessionUser.getId());
        request.setAttribute("love", loveDetailDTO);
        // fas fa-heart text-danger
        // far fa-heart
        // request.setAttribute("css", "far fa-heart");

        return "board/detail";
    }
}