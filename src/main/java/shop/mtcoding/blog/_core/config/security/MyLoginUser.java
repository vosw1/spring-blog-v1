package shop.mtcoding.blog._core.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.mtcoding.blog.user.User;

import java.util.Collection;

// 세션에 저장되는 오브젝트
@Getter
@RequiredArgsConstructor
public class MyLoginUser implements UserDetails {
    private final User user; // 컴포지션 - 결합


    @Override
    public String getPassword() {
        return user.getPassword(); // DB에서 조회된 값을 넣어야함 → 컴포지션 해야 함
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 권한! 나중에 하자.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}