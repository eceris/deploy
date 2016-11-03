package com.daou.deploy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "ds_users")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String gitGroup;
    //삭제했을 경우 row를 지우지 않고 status를 delete로 업데이트 하는 것이 좋을 것 같다.
}
