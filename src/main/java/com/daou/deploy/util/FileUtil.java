package com.daou.deploy.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 파일과 관련된 기능을 공통으로 제공
 * 
 * @author eceris
 *
 */
@Slf4j
public class FileUtil {

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
            log.info(e.getMessage());
        }
        return target.toFile();
    }

    public static void delete(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf("."));
    }

    public static String getTarGzFileName(String fileName) {
        return fileName.substring(0, fileName.indexOf(".tar.gz"));
    }

    /**
     * 파일이름에서 정보를 뽑아내기 위한 클래스
     * 
     * @author eceris
     *
     */
    @Data
    public static class CustomPackageNameToken {
        //CUSTOM-bluecom-201611100015.tar.gz
        //TMS_v9.4.0.1_Linux_patch.tar.gz
        //DaouOffice_v2.3.6.2_Linux_install.tar.gz
        String name; //CUSTOM-bluecom-201611100015
        String category; // CUSTOM
        String project; // bluecom
        String version; // 201611100015
    }

    public static CustomPackageNameToken getCustomPackageNameToken(String fileName) {
        CustomPackageNameToken customPackageNameToken = new CustomPackageNameToken();
        String file = getTarGzFileName(fileName);
        String[] tokens = file.split("-");
        customPackageNameToken.setName(fileName);
        customPackageNameToken.setCategory(tokens[0]);
        customPackageNameToken.setProject(tokens[1]);
        customPackageNameToken.setVersion(tokens[2]);
        return customPackageNameToken;
    }

    @Data
    public static class StandardPackageNameToken {
        //CUSTOM-bluecom-201611100015.tar.gz
        //TMS_v9.4.0.1_Linux_patch.tar.gz
        //DaouOffice_v2.3.6.2_Linux_install.tar.gz
        String name; // TMS_v9.4.0.1_Linux_patch
        String category; // TMS
        String version; // 9.4.0.1
        String type; // install
    }

    public static StandardPackageNameToken getStandardPackageNameToken(String fileName) {
        StandardPackageNameToken standardPackageNameToken = new StandardPackageNameToken();
        String file = getTarGzFileName(fileName);
        String[] tokens = file.split("_");
        standardPackageNameToken.setName(fileName);
        standardPackageNameToken.setCategory(tokens[0]);
        standardPackageNameToken.setVersion(tokens[1].replace("v", ""));
        standardPackageNameToken.setType(tokens[3]);
        return standardPackageNameToken;
    }

}
