package com.daou.deploy.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.daou.deploy.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    User user;

    public AccountUserDetails(User account) {
        this.user = account;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities = {}", user.getRole());
        return Arrays.asList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    public Long getId() {
        return user.getId();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getLoginId();
    }

    public String getLoginId() {
        return user.getLoginId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getGroup() {
        return user.getGitGroup();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
