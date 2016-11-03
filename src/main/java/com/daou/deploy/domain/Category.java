package com.daou.deploy.domain;

import org.springframework.util.StringUtils;

public enum Category {
    DO("daouoffice"), CUSTOM("custom"), TMS("tms");

    private String value;

    private Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Category byValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (Category category : values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        return null;
    }
}
