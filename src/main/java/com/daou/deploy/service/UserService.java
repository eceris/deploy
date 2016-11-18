package com.daou.deploy.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.deploy.domain.Role;
import com.daou.deploy.domain.User;
import com.daou.deploy.domain.model.PageInfo;
import com.daou.deploy.domain.model.PageModel;
import com.daou.deploy.domain.model.UserModel;
import com.daou.deploy.repository.UserRepository;
import com.daou.deploy.util.StringUtil;
import com.google.common.collect.Lists;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 유저 생성
     * 
     * @param userModel
     * @return User
     */
    @Transactional
    public User create(UserModel.Request userModel) {
        User user = modelMapper.map(userModel, User.class);
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole(Role.parseByName(userModel.getRole()));
        return userRepository.save(user);
    }

    /**
     * 유저 조회
     * 
     * @param id
     * @return User
     */
    public User get(Long id) {
        return userRepository.findOne(id);
    }

    /**
     * 유저 수정
     * 
     * @param model
     * @return String
     */
    @Transactional
    public User update(UserModel.Request model) {
        User user = get(model.getId());
        if (isDuplicatedEmail(model.getEmail()) && StringUtil.notEquals(user.getEmail(), model.getEmail())) {
            throw new RuntimeException("DUPLICATED EMAIL");
        }
        user.setEmail(model.getEmail());
        user.setName(model.getName());
        if (StringUtil.isNotEmpty(model.getPassword())) {
            user.setPassword(passwordEncoder.encode(model.getPassword()));
        }
        user.setGitGroup(model.getGitGroup());
        user.setRole(Role.parseByName(model.getRole()));
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User changePassword(Long userId, String password) {
        User user = get(userId);
        if (StringUtil.isNotEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.save(user);
        return user;
    }

    /**
     * 유저 삭제
     * 
     * @param id
     * @return User
     */
    @Transactional
    public void delete(Long id) {
        User user = get(id);
        userRepository.delete(user);
    }

    /**
     * 유저 목록 조회
     * 
     * @return List<User>
     */
    public PageModel<UserModel.Response> getUsers(int page, int offset, String property, String direction) {

        Page<User> userPage = userRepository
                .findAll(new PageRequest(page, offset, Direction.fromString(direction), property));

        List<UserModel.Response> users = Lists.newArrayList();
        for (User user : userPage.getContent()) {
            users.add(modelMapper.map(user, UserModel.Response.class));
        }
        return new PageModel<UserModel.Response>(users, new PageInfo(userPage));
    }

    /**
     * 변경하려는 email값이 이미 있는지 확인
     * 
     * @param email
     * @return boolean
     */
    private boolean isDuplicatedEmail(String email) {
        if (userRepository.findByEmail(email) != null) {
            return true;
        }
        return false;
    }
}
