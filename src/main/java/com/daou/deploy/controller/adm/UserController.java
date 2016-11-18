package com.daou.deploy.controller.adm;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.daou.deploy.domain.User;
import com.daou.deploy.domain.model.PageModel;
import com.daou.deploy.domain.model.UserModel;
import com.daou.deploy.domain.model.UserModel.Response;
import com.daou.deploy.service.UserService;

@Controller(value = "admUserController")
@RequestMapping("/adm")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<UserModel> createUser(@RequestBody @Valid UserModel.Request userModel, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User user = service.create(userModel);
        return new ResponseEntity<UserModel>(modelMapper.map(user, UserModel.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserModel> getUser(@PathVariable("id") Long id) {
        User user = service.get(id);
        return new ResponseEntity<UserModel>(modelMapper.map(user, UserModel.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<PageModel<Response>> getUserList(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "offset", defaultValue = "20") int offset,
            @RequestParam(value = "property", defaultValue = "createdAt") String property,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        PageModel<Response> users = service.getUsers(page, offset, property, direction);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> updateUser(@RequestBody @Valid UserModel.Request userModel, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        User user = service.update(userModel);
        return new ResponseEntity<>(modelMapper.map(user, UserModel.Response.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserModel> deleteUser(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
