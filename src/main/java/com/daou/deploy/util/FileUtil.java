package com.daou.deploy.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

/**
 * 파일과 관련된 기능을 공통으로 제공
 * 
 * @author eceris
 *
 */
public class FileUtil {

    static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static File move(String sourceStr, String targetStr) {
        // source : /opt/Deploy/attach/tmp/CUSTOM-bluecom-201611100015.tar.gz
        // target : /opt/Deploy/attach/custom/bluecom/CUSTOM-bluecom-201611100015.tar.gz
        Path source = Paths.get(sourceStr);
        Path target = Paths.get(targetStr);
        Path parent = target.getParent();
        try {
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return target.toFile();
    }

    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf("."));
    }

    /**
     * 파일이름에서 정보를 뽑아내기 위한 클래스
     * 
     * @author eceris
     *
     */
    @Data
    public static class PackageNameToken {
        //CUSTOM-bluecom-201611100015.tar.gz 
        String name; //CUSTOM-bluecom-201611100015
        String category; // CUSTOM
        String project; // bluecom
        String revision; // 201611100015
    }

    public static PackageNameToken getPackageNameToken(String fileName) {
        PackageNameToken packageNameToken = new PackageNameToken();
        String file = getFileName(fileName);
        String[] tokens = file.split("-");
        packageNameToken.setName(fileName);
        packageNameToken.setCategory(tokens[0]);
        packageNameToken.setProject(tokens[1]);
        packageNameToken.setRevision(tokens[2]);
        return packageNameToken;
    }

}
