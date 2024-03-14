package com.windmeal.global.S3;


import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.windmeal.global.S3.exception.S3Exception;
import com.windmeal.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.windmeal.global.exception.ErrorCode.S3_ERROR;

@Service
@RequiredArgsConstructor
public class S3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;

    public String imgUpload(MultipartFile file) {
        if(file==null) return getDefaultUrl();
        if(!file.getContentType().contains("image")) throw new S3Exception(ErrorCode.S3_TYPE_EXCEPTION,"이미지만 업로드할 수 있습니다.");

        String fileName = UUID.randomUUID().toString();
        fileUpload(file, fileName);
        return fileName;
    }

    private String getDefaultUrl() {
        return "none";
    }

    private void fileUpload(MultipartFile file, String fileName) {
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getBytes().length);

            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException | AmazonClientException e) {
            throw new S3Exception(S3_ERROR,"잠시 후 다시 시도해 주세요.");
        }
    }

    public void delete(String fileName) {
        try{
            s3Client.deleteObject(bucket , fileName);
        }catch (AmazonClientException e) {
            throw new S3Exception(S3_ERROR,"잠시 후 다시 시도해 주세요.");
        }
    }

}
