package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private int id;

    @Column(length = 20) // 20자 제한
    private String title;

    @Column(length = 20) // 20자 제한
    private String content;

    private String author;
}
