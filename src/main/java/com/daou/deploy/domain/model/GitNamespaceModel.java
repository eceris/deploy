package com.daou.deploy.domain.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GitNamespaceModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String path;
    private String kind;
}
