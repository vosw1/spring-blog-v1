package shop.mtcoding.blog.user;

import lombok.Data;

/**
 * 요청 DTO = Data Transfer Object
 * dto 클라이언트로 받는 데이터
 */
public class UserRequest {//유저한테 요청하는 데이터를 할때, 따로 클래스를 분리하면 지저분해보이기 때문에 합쳐서 만듬

    @Data
    public static class joinDTO{//요청받는 항아리 , 유저한테 요구하는 조인 데이터
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class loginDTO{//유저한테 요구하는 로그인 데이터
        private String username;
        private String password;
    }

}