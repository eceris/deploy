package com.daou.deploy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daou.deploy.config.SecurityContext;
import com.daou.deploy.security.AccountUserDetails;

@Controller
public class LoginController {

    @Autowired
    SecurityContext securityContext;

    @RequestMapping("/")
    public String login(Map<String, Object> model) {
        Authentication authentication = securityContext.getAuthentication();
        if (authentication.getPrincipal() instanceof AccountUserDetails) {
            return home(model);
        }
        return "/login";
    }

    @RequestMapping("/home")
    public String home(Map<String, Object> model) {
        return "/home";
    }
}
