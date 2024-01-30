package shop.mtcoding.blog.user;

import lombok.Data;

import javax.persistence.*;

@Data // getter, setter, toString
@Entity //
@Table(name = "user_tb")
public class User { // user DB값 담기
    @Id // 프라이머리 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increament
    private int id;

    @Column(unique=true)
    private String username;

    @Column(length = 60, nullable = false) // 길이 조정, 널 불가
    private String password;
    private String email;

}
