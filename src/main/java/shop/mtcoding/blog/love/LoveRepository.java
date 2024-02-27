package shop.mtcoding.blog.love;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.Board;

@RequiredArgsConstructor
@Repository
public class LoveRepository {
    private final EntityManager em;

    public Love findById(int id) {
        Query query = em.createNativeQuery("select * from love_tb where id = ?", Love.class);
        query.setParameter(1, id);

        Love love = (Love) query.getSingleResult();
        return love;
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from love_tb where id = ?");
        query.setParameter(1, id);

        query.executeUpdate();
    }

    public LoveResponse.DetailDTO findLove(int boardId) { // 카운트만 받기
        String q = """
                SELECT count(*) loveCount
                FROM love_tb
                WHERE board_id = ?;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, boardId);

        // 한건만 받을때는 바로 받기
        Long loveCount = (Long) query.getSingleResult();
        Integer id = 0;
        Boolean isLove = false;

        System.out.println("id : " + id);
        System.out.println("isLove : " + isLove);
        System.out.println("loveCount : " + loveCount);

        LoveResponse.DetailDTO responseDTO = new LoveResponse.DetailDTO(
                id, isLove, loveCount
        );
        return responseDTO;
    }

    public LoveResponse.DetailDTO findLove(int boardId, int sessionUserId) {
        String q = """
                SELECT
                    id,
                    CASE
                        WHEN user_id IS NULL THEN FALSE
                        ELSE TRUE
                    END AS isLove,
                    (SELECT COUNT(*) FROM love_tb WHERE board_id = ?) AS loveCount
                FROM
                    love_tb
                WHERE
                    board_id = ? AND user_id = ?;
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, boardId);
        query.setParameter(2, boardId);
        query.setParameter(3, sessionUserId);

        Integer id = null;
        Boolean isLove = null;
        Long loveCount = null; // long은 null을 사용할 수 없음, Long은 1L로 받아야 함

        try { // 정상적으로 받기
            Object[] row = (Object[]) query.getSingleResult();
            id = (Integer) row[0];
            isLove = (Boolean) row[1];
            loveCount = (Long) row[2];
        } catch (Exception e) { // 댓글이 없어서 터지면 0으로 초기화
            id = 0;
            isLove = false;
            loveCount = 0L;
        }

        System.out.println("id : " + id);
        System.out.println("isLove : " + isLove);
        System.out.println("loveCount : " + loveCount);

        LoveResponse.DetailDTO responseDTO = new LoveResponse.DetailDTO(
                id, isLove, loveCount
        );
        return responseDTO;
    }
}