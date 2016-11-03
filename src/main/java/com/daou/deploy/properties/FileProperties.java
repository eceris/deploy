package com.daou.deploy.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties
public class FileProperties {

    @Value("${default.filePath:C:/Users/daou/Desktop/store}")
    @Getter
    private String defaultPath;

    @Value("${filebox.filePath:C:/Users/daou/Desktop/store/filebox}")
    @Getter
    private String fileboxPath;

    @Value("${package.filePath:./src/test/resources}")
    @Getter
    private String packagePath;
}
