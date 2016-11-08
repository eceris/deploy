package com.daou.deploy.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.daou.deploy.domain.Attach;
import com.daou.deploy.domain.Category;
import com.daou.deploy.domain.Package;
import com.daou.deploy.domain.Project;
import com.daou.deploy.domain.model.PageModel;
import com.daou.deploy.repository.AttachRepository;
import com.daou.deploy.repository.PackageRepository;
import com.daou.deploy.repository.ProjectRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 패키지를 서비스하는 로직(커스텀, 표준판)
 * 
 * @author eceris
 *
 */
@Service
public class PackageService {

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AttachRepository attachRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    CommandService commandService;

    /**
     * 패키지 다운로드
     * 
     * @return
     */
    public File donwload(Long id) {
        Package pkg = packageRepository.findOne(id);
        String path = pkg.getAttach().getPath();
        return new File(path);
    }

    /**
     * 패키지 삭제
     */
    public void delete(Long id) {
        Package pkg = packageRepository.findOne(id);
        Attach attach = pkg.getAttach();
        if (attach != null) {
            File file = new File(attach.getPath());
            file.delete();
            attachRepository.delete(attach.getId());
        }
        packageRepository.delete(id);
    }

    /**
     * 프로젝트별 패키지 조회
     */
    public PageModel<Package> get(Long id, PageRequest page) {
        Project project = projectRepository.findByGitId(id);
        Page<Package> pkgs = packageRepository.findByProject(project, page);
        return new PageModel<Package>(pkgs);
    }

    /**
     * 패키지 검색
     */
    public void search() {
        //TODO
        //근데 필요할까?
    }

    /**
     * 패키지 저장(패키지 빌드 수행후 /project/{id}로 들어오는데 이 api가 호출 될 경우 inteceptor가 잡아서 특정위치의 파일들을 entitiy화 함)
     */
    public void save() {

    }

    /**
     * 패키지 빌드
     * 
     * @param id
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public void build(Long id) throws JsonParseException, JsonMappingException, IOException {
        Project project = projectService.get(id);
        int revision = new Date().hashCode();
        Attach attach = new Attach("CUSTOM-chuncheon-" + revision + ".tar.gz", "", "tar.gz", 123l);
        Package pkg = new Package("version", project, Long.toString(revision), Category.CUSTOM, attach);
        //        CUSTOM-chuncheon-201611031839.tar.gz
        packageRepository.save(pkg);
    }

    /**
     * 표준판 + 패키지 빌드
     * 
     * @param id
     */
    public void standardbuild(Long id) {
        //TODO
    }

    /**
     * 수정사항 체크
     * 
     * @param id
     */
    public void checksource(HttpServletResponse resp, Long id)
            throws JsonParseException, JsonMappingException, IOException {
        Project project = projectService.get(id);
        String command = "/data/do_source/scan.sh --p=" + project.getName() + " --path=" + project.getSshUrl();
        String output = commandService.executeCommand(resp, command);
    }
}
