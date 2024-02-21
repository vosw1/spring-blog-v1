package com.example.myserver;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HelloController {

    @GetMapping("/user")
    public ApiUtil<?> getUserOne() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        User user = new User(1, "ssar", "1234", "ssar@nate.com");
        return new ApiUtil<>(200, "성공", user);
    }
}