package com.daou.deploy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.daou.deploy.domain.Package;
import com.daou.deploy.domain.model.PageModel;
import com.daou.deploy.service.PackageService;

@Controller
public class PackageController {

    private static final Logger logger = LoggerFactory.getLogger(PackageController.class);

    @Autowired
    private PackageService packageService;

    @RequestMapping(value = "/package/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> fileDownload1(@PathVariable("id") Long id) throws IOException {

        File file = packageService.donwload(id);
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());
        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));

        return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/project/{id}/packages", method = RequestMethod.GET)
    public ResponseEntity<PageModel<Package>> files(@PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "offset", defaultValue = "10") int offset,
            @RequestParam(value = "property", defaultValue = "createdAt") String property,
            @RequestParam(value = "direction", defaultValue = "desc") String direction) {

        PageModel<Package> pkgs = packageService.get(id,
                new PageRequest(page, offset, Direction.fromString(direction), property));
        return new ResponseEntity<PageModel<Package>>(pkgs, HttpStatus.OK);
    }
}
