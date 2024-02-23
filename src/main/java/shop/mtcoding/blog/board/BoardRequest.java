package shop.mtcoding.blog.board;

import lombok.Data;

public class BoardRequest {


    @Data
    public static class SaveDTO {
        private String title;
        private String content;
    }

    // 내용도 파싱 방법도 똑같지만 관리를 따로 할 것이기에 만드는 것이 좋음
    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
    }
}
