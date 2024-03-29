package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.board.BoardResponse;

import java.util.List;

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
            throw new RuntimeException("아이디 혹은 비밀번호를 찾을 수 없습니다");
        }
    }

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=?", User.class); // 알아서 매핑해줌
        query.setParameter(1, username);

        try { // 내부적으로 터지면 터지는 위치를 찾아서 내가 잡으면 됨
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null; // 이래야 안터짐
        }
    }
}