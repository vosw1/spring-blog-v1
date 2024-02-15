package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @AllArgsConstructor
    @Data
    public static class MainDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private LocalDateTime createdAt;
        private Integer rid;
        private Integer boardId;
        private String comment;
    }

    @NoArgsConstructor
    @Data
    public static class BoardDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private LocalDateTime createdAt;

        private List<ReplyDTO> replies = new ArrayList<>();

        public void addReply(ReplyDTO reply){
            replies.add(reply);
        }

        public BoardDTO(Integer id, String title, String content, Integer userId, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.userId = userId;
            this.createdAt = createdAt;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ReplyDTO {
        private Integer rid;
        private Integer boardId;
        private String comment;
    }


    @Data
    public static class DetailDTO {
        private int id;
        private String title;
        private String content;
        private int userId;
        private String username;
    }
}