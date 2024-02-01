package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog._core.Constant;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em; // jpa가 제공해줌

    // 조회니까 트랜잭션 필요없음
    public List<Board> findAll(int page){
        int value = page* Constant.PAGING_COUNT;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?", Board.class);
        query.setParameter(1, value);
        query.setParameter(2, Constant.PAGING_COUNT);

        List<Board> boardList = query.getResultList();
        return boardList;
    } // 이 결과를 리퀘스트에 담고 뷰 화면 가서 뿌리기

    public int findBoardTotalCount() {
        Query query = em.createNativeQuery("select count(*) from board_tb");

        int boardTotalCount= ((Number)query.getSingleResult()).intValue();
        //System.out.println("boardTotalCount : "+boardTotalCount);
        return boardTotalCount;
    }
}
