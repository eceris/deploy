package com.daou.deploy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daou.deploy.domain.Package;
import com.daou.deploy.domain.Project;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    //    Package findByAttachPath(String path);
    //
    //    Package findByAttachId(Long id);

    Page<Package> findByProject(Project project, Pageable page);

    //    List<Package> findByCategoryAndProject(Category category, String project);

    //    List<Package> findByCategoryAndSiteAndVersion(Category category, String site, String version);
    //
    //    List<Package> findByAttachNameIgnoreCaseLike(String searchKey);
    //
    //    List<Package> findByCategoryAndAttachNameIgnoreCaseLike(Category category, String searchKey);
}