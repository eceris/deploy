package com.daou.deploy.domain;

import com.google.common.collect.Lists;

public enum Role {
    ROLE_ADMIN, ROLE_DEVELOPER, ROLE_GUEST;

    public static Role parseByName(String name) {
        for (Role role : Lists.newArrayList(Role.values())) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }
}
