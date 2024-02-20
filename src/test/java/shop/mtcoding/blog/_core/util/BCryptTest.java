package shop.mtcoding.blog._core.util;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {

    


    // $2a$10$r86ew9RpjFF8kNfY1jiqRe
    @Test
    public void gernsault_test() {
        String sault = BCrypt.gensalt();
        System.out.println(sault); // 값이 매번 달라짐
    }

    @Test
    public void hashpw_test(){
        String rawPassword = "1234";
        String encPassword = BCrypt.gensalt();
        System.out.println(encPassword);
    }
}