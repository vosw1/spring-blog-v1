package shop.mtcoding.blog.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

// 댓글쓰기, 댓글삭제, 댓글 목록보기
@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final HttpSession session;
    private final ReplyRepository replyRepository;

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable int id) {
        // 인증하기
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        Reply reply = replyRepository.findById(id);

        // 잘못 요청된 것들 분기 처리하기
        // 댓글이 없거나, 댓글 주인이거나, 댓글 주인이 아니거나
        // 댓글 주인 일때만 허용, 나머지는 다 error

        if(reply == null) {
            return "error/404";
        }

        // 권한 체크
        if (reply.getUserId() != sessionUser.getId()) {
            return  "error/403";
        }

        // 핵심 로직
        replyRepository.deleteById(id);

        return "redirect:/board/" + reply.getBoardId(); // 댓글이 있던 게시판 페이지
    }

    @PostMapping("/reply/save")
    public String write(ReplyRequest.SaveDTO requestDTO) {
        System.out.println(requestDTO);

        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 유효성 검사 (님들이 하세요)

        // 핵심 코드
        replyRepository.save(requestDTO, sessionUser.getId());

        return "redirect:/board/" + requestDTO.getBoardId();
    }
}