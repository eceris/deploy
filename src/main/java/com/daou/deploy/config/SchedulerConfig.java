package com.daou.deploy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.daou.deploy.service.PackageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SchedulerConfig {

    @Autowired
    PackageService packageService;

    //     매일 18시 0분 0초에 실행한다.
    @Scheduled(cron = "0 0 18 * * *")
    // 테스트를 위해 30초에 한번씩 호출
    //    @Scheduled(cron = "*/30 * * * * *")
    public void aJob() {
        log.info("file aging ");
        delete();
    }

    public void delete() {
        packageService.deleteOneWeekAging();
    }
}
