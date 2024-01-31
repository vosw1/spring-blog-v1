package shop.mtcoding.blog.user;

import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository // 내가 new 하지 않아도 메모리에 띄울 수 있음
public class UserRepository {
    private EntityManager em; // 컴포지션

    public UserRepository(EntityManager em) { // 생성자
        this.em = em;
    }

    @Transactional
    public void save(UserRequest.joinDTO requestDTO) { // 컨트롤러는 정보를 전달하면서 때리고 위임함
        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values (?, ?, ?)");
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getEmail());

        query.executeUpdate();
    }

    @Transactional
    public void saveV2(UserRequest.joinDTO requestDTO) {
        User user = new User();// 통신을 통해 받은 데이터를 entity를 만들어서 담아보기
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword());
        user.setEmail(requestDTO.getEmail());

        em.persist(user);
    }

    public User findByUsernameAndPassword(UserRequest.loginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? and password=?", User.class);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());

        User user = (User) query.getSingleResult();
        return user;
    }
}