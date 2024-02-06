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
    public void replySave(ReplydRequest.ReplySaveDTO requestDTO, int id) {
        Query query = em.createNativeQuery("insert into reply_tb (content, user_id, created_at) values (?, ?, NOW())");
        query.setParameter(1, requestDTO.getContent());
        query.setParameter(2, id);

        query.executeUpdate();
    }

}
