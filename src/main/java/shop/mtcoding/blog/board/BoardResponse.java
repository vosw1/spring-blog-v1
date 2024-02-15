package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Timestamp createdAt;
        private Boolean pageOwner; // 페이지 주인 여부

        private List<ReplyDTO> replies = new ArrayList<>();

        // 생성자로 초기화
        public void addReply(ReplyDTO reply){
            replies.add(reply);
        }

        // 생성자로 초기화
        public DetailDTO(Integer id, String title, String content, Integer userId, String username, Timestamp createdAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.userId = userId;
            this.username = username;
            this.createdAt = createdAt;
        }
    }

    @AllArgsConstructor
    @Data
    public static class ReplyDTO {
        private Integer rId;
        private Integer rUserId;
        private String rUsername;
        private String rComment;
        private Boolean rOwner; // 로그인 한 유저가 댓글의 주인인지 여부
    }
}