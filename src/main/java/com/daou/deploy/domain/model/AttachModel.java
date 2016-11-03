package com.daou.deploy.domain.model;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.daou.deploy.domain.Category;

import lombok.Data;

@Data
public class AttachModel {

    protected Long id;

    @Size(min = 3)
    protected String name;

    @NotEmpty
    protected String path;

    @NotEmpty
    protected Long size;

    @NotEmpty
    protected Category category;

    @NotEmpty
    protected String version;

    @NotEmpty
    protected String extension;

    @Data
    public static class Response extends AttachModel {
        private Date createdAt;
        private Date updatedAt;
    }
}
