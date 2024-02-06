package shop.mtcoding.blog.reply;

import jakarta.persistence.Column;
import lombok.Data;

public class ReplydRequest {

    @Data
    public static class ReplySaveDTO {
        @Column(length = 100) // 제약 조건 걸기
        private String content;
    }
}
