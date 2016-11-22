package com.daou.deploy.properties;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties
public class DeployProperties {

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

    @Value("${attachBasePath:/opt/deploy/attach}")
    @Getter
    private String attachBasePath;//attach base path

    //모든 패키지(커스텀, do, tms)가 잠시 머무르는 위치
    public String getAttachTempPath() {
        return Paths.get(attachBasePath, "temp").toString();
    }

    //temp 위치에서 CustomPackageMoveInterceptor에 의해 옮겨진 위치
    public String getCustomPackagePath() {
        return Paths.get(attachBasePath, "custom").toString();
    }

    //temp 위치에서 StandardPackageMoveInterceptor에 의해 옮겨진 위치
    public String getDoPackagePath() {
        return Paths.get(attachBasePath, "daouoffice").toString();
    }

    //temp 위치에서 StandardPackageMoveInterceptor에 의해 옮겨진 위치
    public String getTmsPackagePath() {
        return Paths.get(attachBasePath, "tms").toString();
    }
}
