package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Table(name = "love_tb", uniqueConstraints = { // 복합키 만들기
        @UniqueConstraint(
                name = "love_uk",
                columnNames = {"board_id", "user_id"} // 컬럼명 적어주기
        )})
@Data
@Entity // 테이블 생성하기 위해 필요한 어노테이션
public class Love {
    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 전략
    private Integer id;
    private Integer boardId;
    private Integer userId;
    private Timestamp createdAt;
}