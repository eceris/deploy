package com.daou.deploy.domain.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProjectGroupModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String path;
    private String description;
    private String web_url;
    private List<ProjectTransferModel> projects;
}
