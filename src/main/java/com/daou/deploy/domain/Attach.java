package com.daou.deploy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity(name = "ds_attaches")
public class Attach extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String path;

    @Column(nullable = false)
    private String name;

    @Column
    private Long size;

    @Column(nullable = false)
    private String extension;

    public Attach() {
    }

    public Attach(String name, String path, String ext, Long size) {
        this.name = name;
        this.path = path;
        this.extension = ext;
        this.size = size;
    }
}