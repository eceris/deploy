package com.daou.deploy.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daou.deploy.domain.Attach;
import com.daou.deploy.domain.Package;
import com.daou.deploy.domain.Project;
import com.daou.deploy.domain.model.PageModel;
import com.daou.deploy.repository.AttachRepository;
import com.daou.deploy.repository.PackageRepository;
import com.daou.deploy.repository.ProjectRepository;
import com.daou.deploy.util.CommandExecutor;
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
    CommandExecutor commandExecutor;

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
    @Transactional
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
    @Transactional
    public PageModel<Package> get(Long id, PageRequest page) {
        Project project = projectRepository.findOne(id);
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
    @Transactional
    public Package create(Package pkg) {
        return packageRepository.save(pkg);
    }

    /**
     * 패키지 빌드
     * 
     * @param id
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public void build(HttpServletResponse resp, Long id) throws JsonParseException, JsonMappingException, IOException {
        Project project = projectService.get(id);
        String command = "/opt/deploy/tool/web_make_custom.sh --p=" + project.getPath() + " --path="
                + project.getSshUrl();
        String output = commandExecutor.execute(resp, command);
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
        String command = "/opt/deploy/tool/scan.sh --p=" + project.getPath() + " --path=" + project.getSshUrl();
        String output = commandExecutor.execute(resp, command);
    }

    //    public void buildProject(HttpServletResponse resp, String projectName, String sshUrlToRepo, String version) {
    //        String command = "/data/sites/web_make_custom.sh --p=" + projectName + " --path=" + sshUrlToRepo;
    //        if (StringUtil.isNotEmpty(version)) {
    //            command = command.concat(" --v=" + version);
    //        }
    //        log.info("buildProject command => " + command);
    //        String output = commandService.executeCommand(resp, command);
    //    }
    //
    //    public void scanCustomFile(HttpServletResponse resp, String projectName, String sshUrlToRepo) {
    //        String command = "/data/do_source/scan.sh --p=" + projectName + " --path=" + sshUrlToRepo;
    //        log.info("scanCustomFile command => " + command);
    //        String output = commandService.executeCommand(resp, command);
    //    }
    //    
    //    public Boolean buildStandardProject(String version, String branch, String maild_path, String maild_version,
    //            String site) throws ClientProtocolException, IOException, URISyntaxException {
    //        String baseUrl = projectProp.getJenkinsUrl();
    //        String job = projectProp.getJenkinsJob();
    //        String username = projectProp.getJenkinsUser();
    //        String password = projectProp.getJenkinsPassword();
    //        String token = projectProp.getJenkinsToken();
    //
    //        URIBuilder builder = new URIBuilder(baseUrl + "/job/" + job + "/buildWithParameters");
    //        builder.addParameter("token", token);
    //        builder.addParameter("BRAND", "do");
    //        builder.addParameter("VERSION", version);
    //        builder.addParameter("BRANCH", branch);
    //        builder.addParameter("MAILD_PATH", "/data/buildadm/tms");
    //        builder.addParameter("MAILD_VERSION", "");
    //        builder.addParameter("SITES_NAME", site);
    //        builder.addParameter("IS_DEPLOY", "");
    //        String buildUrl = builder.build().toString();
    //
    //        log.info("buildProject url => " + buildUrl);
    //        URI uri = URI.create(buildUrl);
    //        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
    //        CredentialsProvider credsProvider = new BasicCredentialsProvider();
    //        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),
    //                new UsernamePasswordCredentials(username, password));
    //        AuthCache authCache = new BasicAuthCache();
    //        BasicScheme basicAuth = new BasicScheme();
    //        authCache.put(host, basicAuth);
    //        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
    //        HttpPost httPost = new HttpPost(uri);
    //        HttpClientContext localContext = HttpClientContext.create();
    //        localContext.setAuthCache(authCache);
    //        HttpResponse response = httpClient.execute(host, httPost, localContext);
    //        return HttpStatus.CREATED.value() == response.getStatusLine().getStatusCode();
    //    }
    //
    //    public ValueModel getVersionInfo(HttpServletResponse resp, String project) throws Exception {
    //        String command = "/data/distribute/3rd/python33/bin/python3 /data/do_source/parseVersion.py " + project;
    //
    //        log.info("parseVersion command => " + command);
    //        StringBuffer sb = new StringBuffer();
    //        Process p = Runtime.getRuntime().exec(command);
    //        p.waitFor();
    //
    //        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //
    //        String line = "";
    //        while ((line = reader.readLine()) != null) {
    //            sb.append(line + "\n");
    //
    //        }
    //        return new ValueModel("version", sb.toString());
    //    }
}
