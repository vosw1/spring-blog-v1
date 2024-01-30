package shop.mtcoding.blog.user;

import lombok.Data;

/*
 * 요청 DTO = Data Transfer Object
 * 통신으로 전송되는 것을 항아리에 담아서 관리하는 것
 * 응답은 다 파일로 할 것이기에 적지 않음
 * */

public class UserRequest {

    @Data // getter, setter 다 가지고 있음
    public static class joinDTO { // userController에게 요청되는 회원가입 데이터
        private  String username;
        private  String password;
        private  String email;
    }

    @Data // getter, setter 다 가지고 있음
    public static class loginDTO { // userController에게 요청되는 로그인 데이터
        private  String username;
        private  String password;
    }
}
