package shop.mtcoding.blog.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data //GETTER, SETTER, toString 포함된 어노테이션
@Entity // DB의 테이블과 매핑되는 클래스 임을 알려줌
@Table(name = "user_tb") //테이블 이름 : user_tb
public class User { // use_tb의 내용 DB에 담기
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column(unique=true) // 유니크 설정
    private String username;

    @Column(length = 60, nullable = false) // 길이 조정, 널 불가
    private String password;
    private String email;

    // 자바 : 카멜 표기법 -> DB : _ (언더스코어 기법)
    private LocalDateTime createdAt; // created_at
}