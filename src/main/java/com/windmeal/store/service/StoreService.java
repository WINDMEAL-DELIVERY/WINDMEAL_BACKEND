package com.windmeal.store.service;


import com.windmeal.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


}
