package shop.mtcoding.blog.reply;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final HttpSession session; // DI
    private final ReplyRepository replyRepository; // DI

    @PostMapping("/reply/save")
    public String replySave(ReplydRequest.ReplySaveDTO requestDTO, HttpServletRequest request) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        System.out.println("sessionUser:" + sessionUser);
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 댓글을 쓰기 위해 로그인이 필요함
        }

        //2. 바디 데이터 확인 및 유효성 검사
        System.out.println("requestDTO: "+requestDTO);
        System.out.println("requestDTO.getContent(): "+requestDTO.getContent());
        if (requestDTO.getContent().length() > 100) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "댓글의 길이가 100자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }
        // 3. 모델 위임
        // insert into reply_tb (content, user_id, board_id,  created_at) values (?, ?, ? NOW());
        replyRepository.replySave(requestDTO, sessionUser.getId());
        return "redirect:/board/{{boardId}}";
    }
}

