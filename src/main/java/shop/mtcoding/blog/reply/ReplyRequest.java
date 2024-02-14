package shop.mtcoding.blog.reply;

import lombok.Data;

public class ReplyRequest {

    @Data
    public static class SaveDTO {
        private String comment;
        private int boardId;
        // userId는 session에서 가져올 것임
    }
}
