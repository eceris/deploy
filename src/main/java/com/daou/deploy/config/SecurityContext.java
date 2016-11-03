package com.daou.deploy.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.daou.deploy.security.AccountUserDetails;

@Component
public class SecurityContext extends SecurityContextHolder {

    public Authentication getAuthentication() {
        return getContext().getAuthentication();
    }

    public AccountUserDetails getAuthenticationUser() {
        return (AccountUserDetails) getContext().getAuthentication().getPrincipal();
    }

    public String getAuthenticationUserLoginId() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    public String getAuthenticationUserGroup() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getGroup();
    }

    public String getAuthenticationUserEmail() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getEmail();
    }

    public Long getAuthenticationUserId() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public String getAuthenticationUserPassword() {
        AccountUserDetails user = (AccountUserDetails) getContext().getAuthentication().getPrincipal();
        return user.getPassword();
    }

}
