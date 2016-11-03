package com.daou.deploy.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties
public class LegacyConnectionProperties {

    @Value("${gitUrl:http://git.daouoffice.co.kr}")
    @Getter
    private String gitUrl;//gitLab url

    @Value("${privateToken:sMvjFVyDAy_D-VxxBbJx}")
    @Getter
    private String privateToken;//gitLab token

    @Value("${jenkinsUrl:http://build.daouoffice.co.kr}")
    @Getter
    private String jenkinsUrl;//jenkins url

    @Value("${jenkinsJob:1-Sites-Packaging}")
    @Getter
    private String jenkinsJob;//jenkins Job

    @Value("${jenkinsUser:deploy}")
    @Getter
    private String jenkinsUser;//jenkins User id

    @Value("${jenkinsPassword:aorwnclzls2014!@}")
    @Getter
    private String jenkinsPassword;//jenkins User password

    @Value("${jenkinsToken:8a79336cd4d86497927a20ed7a519f61}")
    @Getter
    private String jenkinsToken;//jenkins api token
}
