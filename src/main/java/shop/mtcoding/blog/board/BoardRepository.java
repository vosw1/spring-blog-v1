package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog._core.Constant;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em; // jpa가 제공해줌

    // 조회니까 트랜잭션 필요없음
    public List<Board> findAll(int page) {
        int value = page * Constant.PAGING_COUNT;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?", Board.class);
        query.setParameter(1, value);
        query.setParameter(2, Constant.PAGING_COUNT);

        List<Board> boardList = query.getResultList();
        return boardList;
    } // 이 결과를 리퀘스트에 담고 뷰 화면 가서 뿌리기

    public int findBoardTotalCount() {
        Query query = em.createNativeQuery("select count(*) from board_tb");
        Long boardTtalCount = (Long) query.getSingleResult();
        return boardTtalCount.intValue();
    }

    public BoardResponse.DetailDTO findById(int id) { // 조인해서 응답
        // JpaResultMapper가 헷갈리지 않게 필요한 컬럼명을 적어주기
        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.created_at, bt.user_id, ut.username from board_tb bt inner join user_tb ut on bt.user_id = ut.id where bt.id = ?");
        query.setParameter(1, id);
        JpaResultMapper rm = new JpaResultMapper(); // 컬럼명을 보고 매핑을 해줌
        BoardResponse.DetailDTO responseDTO = rm.uniqueResult(query, BoardResponse.DetailDTO.class);// pk니까 한개 - 오브젝트(테이블)로 받음
        return responseDTO;
    }
}
