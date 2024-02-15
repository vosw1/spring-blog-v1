package shop.mtcoding.blog.board;

import com.sun.tools.javac.Main;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<BoardResponse.MainDTO> findAllV3() {
        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.user_id, bt.created_at, ifnull(rt.id, 0) rid, rt.board_id, ifnull(rt.comment,'')  from board_tb bt left outer join reply_tb rt on bt.id = rt.board_id");

        List<Object[]> rows = (List<Object[]>) query.getResultList();

        List<BoardResponse.MainDTO> mainList = new ArrayList<>();

        for (Object[] row : rows) {
            Integer id = (Integer) row[0];
            String title = (String) row[1];
            String content = (String) row[2];
            Integer userId = (Integer) row[3];
            Timestamp createdAt = (Timestamp) row[4];
            Integer rid = (Integer) row[5];
            Integer boardId = (Integer) row[6];
            String comment = (String) row[7];

            BoardResponse.MainDTO main = new BoardResponse.MainDTO(
                    id, title, content, userId, createdAt.toLocalDateTime(),rid, boardId, comment
            );
            mainList.add(main);
        }

        return mainList;
    }


    public List<BoardResponse.BoardDTO> findAllV2() {
        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.user_id, bt.created_at, ifnull(rt.id, 0) rid, rt.board_id, ifnull(rt.comment,'')  from board_tb bt left outer join reply_tb rt on bt.id = rt.board_id");

        List<Object[]> rows = (List<Object[]>) query.getResultList();

        List<BoardResponse.BoardDTO> boardList = new ArrayList<>();
        List<BoardResponse.ReplyDTO> replyList = new ArrayList<>();

        for (Object[] row : rows) {
            // BoardDTO
            Integer id = (Integer) row[0];
            String title = (String) row[1];
            String content = (String) row[2];
            Integer userId = (Integer) row[3];
            Timestamp createdAt = (Timestamp) row[4];

            BoardResponse.BoardDTO board = new BoardResponse.BoardDTO(
                    id, title, content, userId, createdAt.toLocalDateTime()
            );
            boardList.add(board);

            // ReplyDTO
            Integer rid = (Integer) row[5];
            Integer boardId = (Integer) row[6];
            String comment = (String) row[7];

            BoardResponse.ReplyDTO reply = new BoardResponse.ReplyDTO(
                    rid, boardId, comment
            );

            replyList.add(reply);
        }

        // 크기 4
        boardList = boardList.stream().distinct().toList();

        for (BoardResponse.BoardDTO b : boardList){

            // 6 바퀴
            for (BoardResponse.ReplyDTO r : replyList){
                if(b.getId() == r.getBoardId()){
                    b.addReply(r);
                }
            }
        }

        return boardList;
    }


    public List<Board> findAll() {
        Query query = em.createNativeQuery("select * from board_tb order by id desc", Board.class);
        return query.getResultList();
    }
    public Board findById(int id) {
        Query query = em.createNativeQuery("select * from board_tb where id = ?", Board.class);
        query.setParameter(1, id);
        Board board = (Board) query.getSingleResult();
        return board;
    }
    public BoardResponse.DetailDTO findByIdWithUser(int idx) {
        Query query = em.createNativeQuery("select b.id, b.title, b.content, b.user_id, u.username from board_tb b inner join user_tb u on b.user_id = u.id where b.id = ?");
        query.setParameter(1, idx);
        Object[] row = (Object[]) query.getSingleResult();
        Integer id = (Integer) row[0];
        String title = (String) row[1];
        String content = (String) row[2];
        int userId = (Integer) row[3];
        String username = (String) row[4];
        System.out.println("id : " + id);
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        System.out.println("userId : " + userId);
        System.out.println("username : " + username);
        BoardResponse.DetailDTO responseDTO = new BoardResponse.DetailDTO();
        responseDTO.setId(id);
        responseDTO.setTitle(title);
        responseDTO.setContent(content);
        responseDTO.setUserId(userId);
        responseDTO.setUsername(username);
        return responseDTO;
    }
    @Transactional
    public void save(BoardRequest.SaveDTO requestDTO, int userId) {
        Query query = em.createNativeQuery("insert into board_tb(title, content, user_id, created_at) values(?,?,?, now())");
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, userId);
        query.executeUpdate();
    }
    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }
    @Transactional
    public void update(BoardRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update board_tb set title=?, content=? where id = ?");
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, id);
        query.executeUpdate();
    }
}