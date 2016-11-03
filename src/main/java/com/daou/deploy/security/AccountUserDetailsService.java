package com.daou.deploy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daou.deploy.domain.User;
import com.daou.deploy.repository.UserRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = repository.findByLoginId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException(loginId);
        }
        return new AccountUserDetails(user);
    }
}
