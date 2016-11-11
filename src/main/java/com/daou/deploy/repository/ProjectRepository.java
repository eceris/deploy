package com.daou.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daou.deploy.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByName(String name);
}
