package shop.mtcoding.blog.love;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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


    public LoveResponse.DetailDTO findLove(int boardId) {
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
        Long loveCount = null;
        try {
            Object[] row = (Object[]) query.getSingleResult();
            id = (Integer) row[0];
            isLove = (Boolean) row[1];
            loveCount = (Long) row[2];
        } catch (Exception e) {
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

//    @Transactional // insert된 row 자체를 리턴해줌->insert 시점에서는 알 수 없음 /pk를 알 기 위해서임
    public Love save(LoveRequest.SaveDTO requestDTO, int sessionUserId) {
        Query query = em.createNativeQuery("insert into love_tb(board_id, user_id, created_at) values(?,?, now())");
        query.setParameter(1, requestDTO.getBoardId());
        query.setParameter(2, sessionUserId);

        query.executeUpdate();
        // 같은 트랜잭션 안에 들어가야함
        Query q = em.createNativeQuery("select select * from love_tb where id=(max(id) from love_tb))", Love.class); // max값 조회해서 삽입한 현재값으로 id를 받아와야 삭제도 가능함
        Love love = (Love) q.getSingleResult();
        return love;
    }
}