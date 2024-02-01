package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em; // jpa가 제공해줌

    // 조회니까 트랜잭션 필요없음
    public List<Board> findAll() { // 보드 테이블의 모든 것 가지고 오기
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit 0,3", Board.class);
        List<Board> boardList = query.getResultList(); // 여러건
        return boardList;
    } // 이 결과를 리퀘스트에 담고 뷰 화면 가서 뿌리기
}
