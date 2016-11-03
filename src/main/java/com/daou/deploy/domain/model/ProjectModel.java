package com.daou.deploy.domain.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProjectModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private String sshUrl;
    private String httpUrl;
    private String webUrl;
    private String name;
    private Date createdAt;
    private Long creatorId;
    private String path;

    public ProjectModel(ProjectTransferModel model) {
        this.id = model.getId();
        this.description = model.getDescription();
        this.sshUrl = model.getSsh_url_to_repo();
        this.httpUrl = model.getHttp_url_to_repo();
        this.webUrl = model.getWeb_url();
        this.name = model.getName();
        this.createdAt = model.getCreated_at();
        this.creatorId = model.getCreator_id();
        this.path = model.getPath();
    }
}
