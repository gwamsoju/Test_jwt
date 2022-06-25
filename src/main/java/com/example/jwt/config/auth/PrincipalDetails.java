package com.example.jwt.config.auth;

import com.example.jwt.vo.User2;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {

    private User2 user2;

    public PrincipalDetails(User2 user2) {
        this.user2 = user2;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        user2.getRoleLists().forEach( r -> {
           collection.add(() -> r);
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user2.getPassword();
    }

    @Override
    public String getUsername() {
        return user2.getUsername();
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
}
