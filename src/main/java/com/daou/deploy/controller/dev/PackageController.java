package com.daou.deploy.controller.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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

import com.daou.deploy.domain.Category;
import com.daou.deploy.domain.Package;
import com.daou.deploy.domain.model.PageModel;
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

    @RequestMapping(value = "/build/{id}", method = RequestMethod.POST)
    public ResponseEntity build(@PathVariable("id") Long id, HttpServletResponse resp) throws IOException {
        packageService.build(resp, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/standardbuild/{id}", method = RequestMethod.GET)
    public ResponseEntity standardbuild(@PathVariable("id") Long id) throws IOException {
        packageService.standardbuild(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/checksource/{id}", method = RequestMethod.POST)
    public ResponseEntity checksource(@PathVariable("id") Long id, HttpServletResponse resp) throws IOException {
        packageService.checksource(resp, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    /standard/*/packages
    @RequestMapping(value = "/standard/{category}/packages", method = RequestMethod.GET)
    public ResponseEntity<PageModel<Package>> checksource(@PathVariable("category") String category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "offset", defaultValue = "10") int offset,
            @RequestParam(value = "property", defaultValue = "createdAt") String property,
            @RequestParam(value = "direction", defaultValue = "desc") String direction) throws IOException {
        PageModel<Package> pkgs = packageService.get(Category.byValue(category),
                new PageRequest(page, offset, Direction.fromString(direction), property));

        return new ResponseEntity<PageModel<Package>>(pkgs, HttpStatus.OK);
    }

    @RequestMapping(value = "/package/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@PathVariable("id") Long id) throws IOException {

        File file = packageService.download(id);
        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());
        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));

        return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
    }

    //    TMS_v9.4.0.1_Linux_patch.tar.gz
    //    DaouOffice_v2.3.6.2_Linux_install.tar.gz
    //    @RequestMapping(value = "/file/package", method = RequestMethod.GET)
    //    public ResponseEntity getPackageList(@RequestParam(value = "page", defaultValue = "0") int page,
    //            @RequestParam(value = "offset", defaultValue = "10") int offset,
    //            @RequestParam(value = "brand") String brand, @RequestParam(value = "type") String downloadType) {
    //
    //        PageModel<PackageModel> packagePage = fileService.getPackageList(page, offset, brand,
    //                PackageDownloadType.byValue(downloadType));
    //        return new ResponseEntity<>(packagePage, HttpStatus.OK);
    //    }
    //
    //    @RequestMapping(value = "/file/package/download", method = RequestMethod.GET)
    //    public ResponseEntity packageDownload(@RequestParam(value = "brand", required = true) String brand,
    //            @RequestParam(value = "name", required = true) String name) throws IOException {
    //
    //        File file = fileService.packageDownloadFile(brand, name, securityContext.getAuthenticationUserLoginId());
    //
    //        if (file == null)
    //            return new ResponseEntity<>("File Not Exist", HttpStatus.OK);
    //
    //        HttpHeaders respHeaders = new HttpHeaders();
    //
    //        respHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    //        respHeaders.setContentLength(file.length());
    //        respHeaders.setContentDispositionFormData("attachment", file.getName());
    //
    //        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
    //
    //        return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
    //    }
}
