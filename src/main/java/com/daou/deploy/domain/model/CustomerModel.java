package com.daou.deploy.domain.model;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

public class CustomerModel {
    protected Long id;

    @NotEmpty
    @Size(min = 3)
    protected String site;

    @Data
    public static class Response extends CustomerModel {
        private Date createdAt;
        private Date updatedAt;
    }
}
