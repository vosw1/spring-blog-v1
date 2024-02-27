package shop.mtcoding.blog.love;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shop.mtcoding.blog._core.util.ApiUtil;
import shop.mtcoding.blog.user.User;

@RequiredArgsConstructor
@RestController // 화면을 줄 일이 없음
public class LoveController {
    private final LoveRepository loveRepository;
    private final HttpSession session;

    @DeleteMapping("/api/love/{id}")
    public ApiUtil<?> delete(@PathVariable Integer id) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return new ApiUtil<>(401, "인증 안됨");
        }
        // 2. 권한 체크
        Love love = loveRepository.findById(id);
        if(love.getUserId() != sessionUser.getId()){
            return new ApiUtil<>(403, "권한 없음");
        }
        loveRepository.deleteById(id);
        return new ApiUtil<>(null); // 성공
    }

}

