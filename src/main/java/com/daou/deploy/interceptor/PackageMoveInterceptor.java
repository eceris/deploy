package com.daou.deploy.interceptor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.daou.deploy.domain.Attach;
import com.daou.deploy.domain.Category;
import com.daou.deploy.domain.Package;
import com.daou.deploy.domain.Project;
import com.daou.deploy.properties.DeployProperties;
import com.daou.deploy.service.PackageService;
import com.daou.deploy.service.ProjectService;
import com.daou.deploy.util.FileUtil;
import com.daou.deploy.util.FileUtil.PackageNameToken;
import com.daou.deploy.util.StringUtil;

import lombok.extern.log4j.Log4j;

/**
 * project/{id} 가 호출될 경우를 인터셉트하여 패키지를 옮긴다
 * 
 * @author eceris
 *
 */
@Log4j
@Component
public class PackageMoveInterceptor implements HandlerInterceptor {

    @Autowired
    DeployProperties properties;

    @Autowired
    ProjectService projectService;

    @Autowired
    PackageService packageService;

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object obj, Exception ex)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object obj, ModelAndView mv)
            throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
        log.info("req.getRequestURI()");
        //아이디를 잡아서 project를 가져옴
        //project의 이름으로 tmp에 떨어진 파일들을 모아서 move후에 entity화
        String attachTmpPath = properties.getAttachTmpPath();
        String customPath = properties.getCustomPackagePath();

        Map pathVariables = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Project project = projectService.get(Long.parseLong(pathVariables.get("id").toString()));
        //                return files;
        log.info(project.getPath());
        File dir = new File(attachTmpPath);
        File[] listFiles = dir.listFiles();
        log.info(listFiles.length);
        for (File file : listFiles) {
            //tmp 폴더중에 조회한 프로젝트의 파일이 있다면
            PackageNameToken packageNameToken = FileUtil.getPackageNameToken(file.getName());
            if (StringUtil.equals(project.getName(), packageNameToken.getProject())) {
                //파일을 옮기고
                FileUtil.move(file.getPath(), Paths.get(customPath, project.getName(), file.getName()).toString());
                Attach attach = new Attach(packageNameToken.getName(), file.getPath(),
                        Files.size(file.toPath()) / 1024);
                //entity로 만들어서 저장
                packageService.create(new Package(null, project, packageNameToken.getRevision(),
                        Category.valueOf(packageNameToken.getCategory()), attach));
            }
        }
        return true;
    }
}
