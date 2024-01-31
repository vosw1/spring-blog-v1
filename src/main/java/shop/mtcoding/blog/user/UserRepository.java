package shop.mtcoding.blog.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

@Repository
public class UserRepository {
    private EntityManager em;


    public UserRepository(EntityManager em) {
        this.em = em;
    }


    @Transactional
    public void save(UserRequest.joinDTO requestDTO) {
        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values (?, ?, ?)");
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        query.setParameter(3, requestDTO.getEmail());
        query.executeUpdate();
    }

    @Transactional
    public void saveV2(UserRequest.joinDTO requestDTO) {
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword());
        user.setEmail(requestDTO.getEmail());
        em.persist(user);
    }
    @Transactional
    public User findByUsernameAndPassword(UserRequest.loginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? and password=?", User.class);
        query.setParameter(1, requestDTO.getUsername());
        query.setParameter(2, requestDTO.getPassword());
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=?", User.class);
        query.setParameter(1, username);
        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}