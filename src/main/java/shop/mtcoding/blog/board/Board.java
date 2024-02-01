package shop.mtcoding.blog.board;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data // getter, setter, toString 포함되어 있음
@Entity // 붙여야 테이블이 만들어지고 조회된게 해당객체로 파싱됨
@Table(name = "board_tb")
public class Board {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto
    private int id;

    private String title;
    private String content; // 데이터가 여러건이라 테이블로 쪼개야함
    private int userId; //카멜을 쓰면 언더스코어로 만들어줌 -> 외래키

    @CreationTimestamp
    private LocalDateTime createdAt;
}
