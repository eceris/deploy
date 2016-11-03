package com.daou.deploy.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.daou.deploy.domain.User;

public class UserTest {

    @Test
    public void getterSetter() {
        User account = new User();
        String email = "thkim@daou.co.kr";
        account.setEmail(email);
        assertThat(account.getEmail(), is(email));
    }

}