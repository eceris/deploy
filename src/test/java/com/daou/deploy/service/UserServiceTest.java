package com.daou.deploy.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.daou.deploy.Application;
import com.daou.deploy.domain.User;
import com.daou.deploy.domain.model.PageModel;
import com.daou.deploy.domain.model.UserModel;
import com.daou.deploy.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateNew_유저_생성_테스트() {

        // GIVEN
        UserModel.Request model = new UserModel.Request();
        model.setName("name");
        model.setLoginId("loginId");
        model.setEmail("email@daou.co.kr");
        model.setPassword("rltnfxla2015!");

        // WHEN
        User user = userService.create(model);

        // THEN
        assertThat(user.getName(), is(model.getName()));
    }

    @Test
    public void testUpdateUser_유저_갱신_테스트() {

        // GIVEN
        UserModel.Request model = new UserModel.Request();
        model.setName("name");
        model.setLoginId("loginId");
        model.setEmail("email@daou.co.kr");
        model.setPassword("1111");
        User user = userService.create(model);
        model.setName("updateName");
        model.setId(user.getId());
        User updateUser = userService.update(model);

        // WHEN
        user = userService.update(model);

        // THEN
        assertThat(user.getName(), is(model.getName()));
    }

    @Test
    public void testDeleteUser_유저_삭제_테스트() {

        // GIVEN
        UserModel.Request model = new UserModel.Request();
        model.setName("name");
        model.setLoginId("loginId");
        model.setEmail("email@daou.co.kr");
        model.setPassword("1111");
        User user = userService.create(model);

        // WHEN
        userService.delete(user.getId());

        // THEN
        Assert.assertNull(user);
    }

    @Test
    public void testGetUser_유저_조회_테스트() {

        // GIVEN
        UserModel.Request model = new UserModel.Request();
        model.setName("name");
        model.setLoginId("loginId");
        model.setEmail("email@daou.co.kr");
        model.setPassword("1111");
        User user = userService.create(model);

        // WHEN
        User readUser = userService.get(user.getId());

        // THEN
        assertThat(readUser.getEmail(), is(user.getEmail()));
    }

    @Test
    public void testGetUserList_유저리스트_조회_테스트() {

        // GIVEN
        UserModel.Request model = new UserModel.Request();

        String[] loginId = {"aaa", "bbb", "ccc", "ddd", "eee"};
        String[] email = {"aaa@daou.co.kr", "bbb@daou.co.kr", "ccc@daou.co.kr", "ddd@daou.co.kr", "eee@daou.co.kr"};

        PageModel<UserModel.Response> beforeList = userService.getUsers(0, 20, "createdAt", "asc"); //Userlist of before adding users
        for (int i = 0; i < 5; i++) {
            model.setName("name");
            model.setLoginId(loginId[i]);
            model.setEmail(email[i]);
            model.setPassword("1111");
            userService.create(model); //adding user
        }

        // WHEN
        PageModel<UserModel.Response> afterList = userService.getUsers(0, 1000, "createdAt", "asc"); //Userlist of before adding users

        // THEN
        Assert.assertEquals(beforeList.getContent().size() + 5, afterList.getContent().size());
    }
}