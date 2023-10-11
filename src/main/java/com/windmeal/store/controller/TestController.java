package com.windmeal.store.controller;

import com.windmeal.store.domain.Store;
import com.windmeal.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final StoreService storeService;
    @PostMapping(value = "/test")
    public Store test() {
        return storeService.save();
    }

    @GetMapping(value = "/test/{storeId}")
    public Store find(@PathVariable Long storeId) {
        return storeService.find(storeId);
    }
}
