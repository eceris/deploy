package com.daou.deploy.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ds_projects")
public class Project {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String path;

    @Column
    private String sshUrl;

    @Column
    private String httpUrl;

    @Column
    private String webUrl;

    @Column
    private Date createdAt;

    @Column
    private String namespace;
}
