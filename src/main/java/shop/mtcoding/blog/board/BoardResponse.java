package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;

public class BoardResponse {

    @Data // DB세상의 데이터 -> 릴레이션 매핑 : Java세상 데이터
    public static class DetailDTO {
        private int id;
        private String title;
        private String content;
        private int userId; // 게시글 작성자 아이디
        private String username;
        private Boolean boardOwner;

        public void isBoardOwner(User sessionUser) { // 로그인한 ID가 필요함
            if (sessionUser == null){
                boardOwner = false;
            } else {
                boardOwner = sessionUser.getId() == userId; // 같은지 확인하기
            }
        }
    }

    @Data
    public static class ReplyDTO {
        private Integer id;
        private Integer userId;
        private String comment;
        private String username;
        private Boolean replyOwner; // 댓글의 작성자 확인(session값과 비교)

        public ReplyDTO(Object[] ob, User sessionUser) {
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.comment = (String) ob[2];
            this.username = (String) ob[3];

            if(sessionUser == null) {
                replyOwner = false;
            } else {
                replyOwner = sessionUser.getId() == userId;
            }
        }
    }
}