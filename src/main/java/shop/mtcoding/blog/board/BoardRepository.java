package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em; // IoC 컨테이너에 주입되어야 합니다.

    public List<Board> findAll() {
        Query query = em.createNativeQuery("select * from board_tb order by id desc", Board.class);
        return query.getResultList();
    }

    @Transactional
    public void save(BoardRequest.SaveDTO requestDTO) {
            Query query = em.createNativeQuery("insert into board_tb(title, content, author) values(?, ?, ?)");
            // 쿼리 완성하기
            query.setParameter(1, requestDTO.getTitle());
            query.setParameter(2, requestDTO.getContent());
            query.setParameter(3, requestDTO.getAuthor());
            // 쿼리 전송하기
            query.executeUpdate();
            System.out.println("query: " + query);
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }
}
