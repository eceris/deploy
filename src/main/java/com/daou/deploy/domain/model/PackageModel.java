package com.daou.deploy.domain.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageModel {
    private String brand;
    private String name;
    private long size;
    private Date createdAt;
}
