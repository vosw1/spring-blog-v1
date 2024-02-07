package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRequest;

@Repository // IoC에 new하는 방법
public class UserRepository {

    // DB에 접근할 수 있는 매니저 객체
    // 스프링이 만들어서 IoC에 넣어둔다.
    // DI에서 꺼내 쓰기만 하면된다.
    private EntityManager em; // 컴포지션

    // 생성자 주입 (DI 코드)
    public UserRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional // db에 write 할때는 필수
    public void save(UserRequest.JoinDTO requestDTO){
        Query query = em.createNativeQuery("insert into user_tb(username, password, email, created_at) values(?,?,?, now())");
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getEmail());
        query.executeUpdate();
    }

    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? and password=?", User.class); // 알아서 매핑해줌
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());

        try { // 내부적으로 터지면 터지는 위치를 찾아서 내가 잡으면 됨
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public User findByIdAndEmail(int id) {
        try {
            Query query = em.createNativeQuery("select username, email from user_tb where id=?");
            query.setParameter(1, id);

            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void userUpdate(UserRequest.UpdateDTO requestDTO, int id){
        Query query = em.createNativeQuery("update user_tb set password=? where id = ?");
        query.setParameter(1,requestDTO.getPassword() );
        query.setParameter(2, id);
        query.executeUpdate();
        System.out.println("query:" + query);
    }
}