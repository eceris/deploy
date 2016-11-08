package com.daou.deploy.controller.dev;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daou.deploy.service.PackageService;

@Controller(value = "devPackageController")
@RequestMapping("/dev")
public class PackageController {

    private static final Logger logger = LoggerFactory.getLogger(PackageController.class);

    @Autowired
    private PackageService packageService;

    @RequestMapping(value = "/package/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws IOException {
        packageService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "/build/{id}", method = RequestMethod.GET)
    public ResponseEntity build(@PathVariable("id") Long id) throws IOException {
        packageService.build(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "/standardbuild/{id}", method = RequestMethod.GET)
    public ResponseEntity standardbuild(@PathVariable("id") Long id) throws IOException {
        packageService.standardbuild(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(value = "/checksource/{id}", method = RequestMethod.GET)
    public ResponseEntity checksource(@PathVariable("id") Long id, HttpServletResponse resp) throws IOException {
        packageService.checksource(resp, id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
