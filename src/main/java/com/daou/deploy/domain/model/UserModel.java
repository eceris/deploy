package com.daou.deploy.domain.model;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    private Long id;

    @NotEmpty
    @Size(min = 3)
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String loginId;

    private String role;

    private String gitGroup;

    @Data
    public static class Request extends UserModel {
        private String password;
    }

    @Data
    public static class Response extends UserModel {
        private Date createdAt;
        private Date updatedAt;
    }
}
