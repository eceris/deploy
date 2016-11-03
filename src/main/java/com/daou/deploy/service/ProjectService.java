package com.daou.deploy.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.daou.deploy.config.SecurityContext;
import com.daou.deploy.domain.Project;
import com.daou.deploy.domain.User;
import com.daou.deploy.domain.model.GitNamespaceModel;
import com.daou.deploy.domain.model.GitProjectModel;
import com.daou.deploy.domain.model.ProjectGroupModel;
import com.daou.deploy.domain.model.ProjectTransferModel;
import com.daou.deploy.properties.LegacyConnectionProperties;
import com.daou.deploy.repository.ProjectRepository;
import com.daou.deploy.repository.UserRepository;
import com.daou.deploy.util.StringUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

/**
 * 
 * 프로젝트 리스트 및 상세를 서비스
 * 
 * @author eceris
 *
 */
@Service
public class ProjectService {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private LegacyConnectionProperties legacyProp;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Project get(Long id) throws JsonParseException, JsonMappingException, IOException {
        Project project = projectRepository.findByGitId(id);
        if (project == null) {
            String gitUrl = legacyProp.getGitUrl();
            String privateToken = legacyProp.getPrivateToken();
            User user = userRepository.findOne(securityContext.getAuthenticationUserId());
            List<String> gitGroups = Lists.newArrayList();
            if (StringUtil.isEmpty(user.getGitGroup())) {
                gitGroups.add("do");
            } else {
                gitGroups.addAll(Arrays.asList(user.getGitGroup().split(";")));
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("private_token", privateToken);
            String result = null;
            GitProjectModel projectModel = null;
            params.put("id", id);
            result = restTemplate.getForObject(gitUrl + "/api/v3/projects/{id}?private_token={private_token}",
                    String.class, params);
            projectModel = objectMapper.readValue(result, GitProjectModel.class);
            System.out.println(projectModel);
            project = new Project();
            project.setCreatedAt(projectModel.getCreated_at());
            project.setGitId(projectModel.getId());
            project.setHttpUrl(projectModel.getHttp_url_to_repo());
            project.setName(projectModel.getName());
            project.setNamespace(projectModel.getNamespace().getPath());
            project.setPath(projectModel.getPath());
            project.setSshUrl(projectModel.getSsh_url_to_repo());
            project.setWebUrl(projectModel.getWeb_url());
            project = projectRepository.save(project);
        }
        return project;
    }

    @Transactional(readOnly = true)
    public ProjectGroupModel getProjects() throws JsonParseException, JsonMappingException, IOException {
        String gitUrl = legacyProp.getGitUrl();
        String privateToken = legacyProp.getPrivateToken();
        User user = userRepository.findOne(securityContext.getAuthenticationUserId());

        List<String> gitGroups = Lists.newArrayList();
        if (StringUtil.isEmpty(user.getGitGroup())) {
            gitGroups.add("do");
        } else {
            gitGroups.addAll(Arrays.asList(user.getGitGroup().split(";")));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("private_token", privateToken);
        String result = null;
        ProjectGroupModel groupModel = null;
        List<ProjectTransferModel> projects = Lists.newArrayList();
        for (String group : gitGroups) {
            params.put("group", group);
            result = restTemplate.getForObject(gitUrl + "/api/v3/groups/{group}?private_token={private_token}",
                    String.class, params);
            groupModel = objectMapper.readValue(result, ProjectGroupModel.class);
            projects.addAll(groupModel.getProjects());
        }

        groupModel.setProjects(projects);
        return groupModel;
    }

    public List<GitNamespaceModel> getGitNamespaces() throws JsonParseException, JsonMappingException, IOException {
        String gitUrl = legacyProp.getGitUrl();
        String privateToken = legacyProp.getPrivateToken();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("private_token", privateToken);
        String result = restTemplate.getForObject(gitUrl + "/api/v3/namespaces?private_token={private_token}",
                String.class, params);
        List<GitNamespaceModel> namespaces = objectMapper.readValue(result,
                objectMapper.getTypeFactory().constructCollectionType(List.class, GitNamespaceModel.class));
        List<GitNamespaceModel> byGroup = Lists.newArrayList();
        for (GitNamespaceModel model : namespaces) {
            if (model.getKind().equalsIgnoreCase("group")) {
                byGroup.add(model);
            }
        }
        return byGroup;
    }
}
