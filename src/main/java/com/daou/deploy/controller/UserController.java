package com.daou.deploy.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daou.deploy.config.SecurityContext;
import com.daou.deploy.domain.User;
import com.daou.deploy.domain.model.UserModel;
import com.daou.deploy.domain.model.ValueModel;
import com.daou.deploy.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    SecurityContext securityContext;

    @RequestMapping(value = "/user/password", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> createUser(@RequestBody ValueModel passwordModel) {
        Long id = securityContext.getAuthenticationUserId();
        User user = service.changePassword(id, passwordModel.getValue());
        return new ResponseEntity<UserModel>(modelMapper.map(user, UserModel.Response.class), HttpStatus.OK);
    }
}
