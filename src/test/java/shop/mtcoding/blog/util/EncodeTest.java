package shop.mtcoding.blog.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeTest {

    @Test
    public void encoded_test() {
        BCryptPasswordEncoder en = new BCryptPasswordEncoder();
        String rawPassword = "1234";

        String encPassword = en.encode(rawPassword);
        System.out.println(encPassword);
        // $2a$10$z18Td.MxcWGbVbITabM83.FGlvN/P5F.taTF1oLoNeddA.HvRQoLa
    }
}
