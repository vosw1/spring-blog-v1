package shop.mtcoding.blog.reply;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;

    @Transactional
    public void save(ReplyRequest.SaveDTO requestDTO, int userId) {
        Query query = em.createNativeQuery("insert into reply_tb(comment, board_id, user_id, created_at) values(?,?,?, now())");
        query.setParameter(1, requestDTO.getComment());
        query.setParameter(2, requestDTO.getBoardId());
        query.setParameter(3, userId);

        query.executeUpdate();
    }
}
