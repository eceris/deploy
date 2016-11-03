package com.daou.deploy.domain.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitProjectModel {
    private Long id;
    private String description;
    private String ssh_url_to_repo;
    private String http_url_to_repo;
    private String web_url;
    private String name;
    private String path;
    private Date created_at;
    private Date updated_at;
    private Namespace namespace;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Namespace {
        private Long id;
        private String name;
        private String path;
    }
}
