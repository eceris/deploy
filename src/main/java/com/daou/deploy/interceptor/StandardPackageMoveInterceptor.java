package com.daou.deploy.interceptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
import com.daou.deploy.util.FileUtil.StandardPackageNameToken;
import com.daou.deploy.util.StringUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

/**
 * project/{id} 가 호출될 경우를 인터셉트하여 패키지를 옮긴다
 * 
 * @author eceris
 *
 */
@Slf4j
@Component
public class StandardPackageMoveInterceptor implements HandlerInterceptor {

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
        sync(req);
        return true;
    }

    @Transactional
    private void sync(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException {
        log.info(req.getRequestURI());
        //category로 daouoffice와 tms 패키지를 구분하여 temp에 떨어진 파일들을 모아서 move후에 entity화
        String attachTmpPath = properties.getAttachTempPath();
        String doPkgPath = properties.getDoPackagePath();
        String tmsPkgPath = properties.getTmsPackagePath();

        Map pathVariables = (Map) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String category = pathVariables.get("category").toString();
        File dir = new File(attachTmpPath);
        for (Object fileObject : ArrayUtils.nullToEmpty(dir.listFiles())) {
            File source = (File) fileObject;
            if (source.getName().startsWith("CUSTOM")) {
                //파일이름이 CUSTOM으로 시작하면 건너뜀
                //여기에서는 TMS, DaouOffice만 동기화 하므로
                continue;
            }
            //temp 폴더중에 조회한 카테고리의 파일이 있다면
            StandardPackageNameToken packageNameToken = FileUtil.getStandardPackageNameToken(source.getName());
            if (StringUtil.equalsIgnoreCase(category, packageNameToken.getCategory())) {
                //category 값에 따라 타겟패키지 디렉토리를 지정하고
                String targetPkgDir = StringUtil.equalsIgnoreCase(packageNameToken.getCategory().toLowerCase(),
                        Category.TMS.name()) ? tmsPkgPath : doPkgPath;

                //파일을 옮기고
                File target = FileUtil.move(source.getPath(), Paths.get(targetPkgDir, source.getName()).toString());
                Attach attach = new Attach(target.getName(), target.getPath(), Files.size(target.toPath()));
                Project project = projectService.getStandardProjectByPath(packageNameToken.getCategory().toLowerCase());
                //entity로 만들어서 저장
                packageService.create(new Package(packageNameToken.getVersion(), project,
                        Category.byValue(packageNameToken.getCategory().toLowerCase()), attach));
            }
        }
    }
}
