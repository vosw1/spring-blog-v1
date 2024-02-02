package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;

    @ManyToOne // 매니는 보드 원은 유저 : 1대 N -> 오류 사라짐
    private User user; // 컬럼에 테이블로 만ㄴ들어지지는 않음

    @CreationTimestamp
    private LocalDateTime createdAt;
}