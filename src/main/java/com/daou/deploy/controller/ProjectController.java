package com.daou.deploy.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daou.deploy.domain.Project;
import com.daou.deploy.domain.model.GitNamespaceModel;
import com.daou.deploy.domain.model.ProjectGroupModel;
import com.daou.deploy.service.ProjectService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ResponseEntity<ProjectGroupModel> getProjects()
            throws JsonParseException, JsonMappingException, IOException {

        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> getProject(@PathVariable(value = "id") Long id)
            throws JsonParseException, JsonMappingException, IOException {

        return new ResponseEntity<>(projectService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/namespaces", method = RequestMethod.GET)
    public ResponseEntity<List<GitNamespaceModel>> getNamespaces()
            throws JsonParseException, JsonMappingException, IOException {

        return new ResponseEntity<>(projectService.getGitNamespaces(), HttpStatus.OK);
    }

    //    @RequestMapping(value = "/project/build", method = RequestMethod.POST)
    //    public void buildProject(@RequestParam String project, @RequestParam String path,
    //            @RequestParam(value = "version", required = false) String version, HttpServletResponse response)
    //            throws JsonParseException, JsonMappingException, IOException {
    //        projectService.buildProject(response, project, path, version);
    //    }
    //
    //    @RequestMapping(value = "/project/scancustomfile", method = RequestMethod.POST)
    //    public void scanCustomFile(@RequestParam String project, @RequestParam String path, HttpServletResponse response)
    //            throws JsonParseException, JsonMappingException, IOException {
    //        projectService.scanCustomFile(response, project, path);
    //    }
    //
    //    @RequestMapping(value = "/project/build/standard", method = RequestMethod.POST)
    //    public ResponseEntity<Boolean> buildStandardProject(@RequestParam String version, @RequestParam String branch,
    //            @RequestParam String maild_path, @RequestParam String maild_version, @RequestParam String site)
    //            throws JsonParseException, JsonMappingException, Exception {
    //
    //        return new ResponseEntity<Boolean>(
    //                projectService.buildStandardProject(version, branch, maild_path, maild_version, site), HttpStatus.OK);
    //    }
    //
    //    @RequestMapping(value = "/project/version", method = RequestMethod.GET)
    //    public ResponseEntity<ValueModel> getVersionInfo(@RequestParam String project, HttpServletResponse response)
    //            throws JsonParseException, JsonMappingException, Exception {
    //        return new ResponseEntity<ValueModel>(projectService.getVersionInfo(response, project), HttpStatus.OK);
    //    }
}
