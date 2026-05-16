package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuBackupRunner implements ApplicationRunner {

    private final MenuService menuService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Taking menu backup on application startup...");
        menuService.getAllMenuBackup();
        log.info("Menu backup completed.");
    }
}