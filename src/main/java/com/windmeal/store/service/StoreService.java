package com.windmeal.store.service;


import com.windmeal.store.domain.Store;
import com.windmeal.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public Store save(){
        return storeRepository.save(new Store());
    }
    @Transactional(readOnly = true)
    public Store find(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow();
    }
}
