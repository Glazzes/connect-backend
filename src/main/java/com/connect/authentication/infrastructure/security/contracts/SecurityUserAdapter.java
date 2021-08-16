package com.connect.authentication.infrastructure.security.contracts;

import com.connect.user.domain.entities.PostgresUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUserAdapter implements UserDetails {
    private final PostgresUser user;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUserAdapter(PostgresUser user){
        this.user = user;
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = null;
    }

    public PostgresUser getUser(){
        return this.user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
