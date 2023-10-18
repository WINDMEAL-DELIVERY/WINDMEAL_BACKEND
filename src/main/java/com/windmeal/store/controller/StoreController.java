package com.windmeal.store.controller;

import com.windmeal.global.S3.S3Util;
import com.windmeal.global.exception.ResultDataResponseDTO;
import com.windmeal.store.dto.request.StoreCreateRequest;
import com.windmeal.store.dto.response.StoreResponse;
import com.windmeal.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final S3Util s3Util;

    @PostMapping("/store")
    public ResultDataResponseDTO createStore(
            @RequestPart StoreCreateRequest request,
            @RequestPart("file") MultipartFile file){

        String imgUrl = s3Util.imgUpload(file);
        StoreResponse response = storeService.createStore(request,imgUrl);
        return ResultDataResponseDTO.of(response);
    }
}
