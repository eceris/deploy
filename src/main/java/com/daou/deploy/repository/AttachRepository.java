package com.daou.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daou.deploy.domain.Attach;

@Repository
public interface AttachRepository extends JpaRepository<Attach, Long> {

}
