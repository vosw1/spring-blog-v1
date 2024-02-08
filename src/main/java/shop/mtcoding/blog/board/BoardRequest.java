package shop.mtcoding.blog.board;

import lombok.Data;

public class BoardRequest {

    @Data
    public static class SaveDTO { // 정보를 담을 가방 만들기
        private String title;
        private String content;
        private String author;
    }

    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
        private String author;
    }
}
