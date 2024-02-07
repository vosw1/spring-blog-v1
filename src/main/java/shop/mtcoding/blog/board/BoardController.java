package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session; // DI
    private final BoardRepository boardRepository; // DI

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
        BoardResponse.DetailDTO reponseDTO = boardRepository.findByIdWithUser(id); //메서드 이름 변경

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