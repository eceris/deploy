package com.daou.deploy.domain.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProjectTransferModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String description;
    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private String web_url;
    private String name;
    private Date created_at;
    private Long creator_id;
    private String path;
    private ProjectNamespaceModel namespace;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public class ProjectNamespaceModel implements Serializable {
        private static final long serialVersionUID = 1L;
        Long id;
        String name;
        String path;
        String description;
    }
}
