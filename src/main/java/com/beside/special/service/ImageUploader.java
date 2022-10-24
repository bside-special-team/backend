package com.beside.special.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageUploader {
    private static final String BUCKET_NAME = "team.special";
    private static final long EXPIRATION_MILLS = 1000L * 60 * 60 * 24 * 7;     // 사진 공개 URL 인증 시간 1/1000초, 최대 7일 설정 가능

    private final AmazonS3 s3;

    // 이미지 업로드 ( MultiPartFile, 저장 디렉토리 )  TODO File Size 처리 (제한?)
    public List<String> uploadImage(List<MultipartFile> images, String targetDirectory) throws IOException {
        List<String> saveFileNames = new ArrayList<>();

        for (MultipartFile file : images) {
            if (file.isEmpty() | !isWebp(file)) {
                throw new RuntimeException("Image 생성 오류");
            }
            String fileKey = targetDirectory + "/" + getImageKey(file);
            try {
                s3.putObject(new PutObjectRequest(
                        BUCKET_NAME,
                        fileKey,
                        file.getInputStream(),
                        new ObjectMetadata()));

                Date expiration = new Date();
                long nowMillis = Instant.now().toEpochMilli();
                nowMillis += EXPIRATION_MILLS;
                expiration.setTime(nowMillis);

                GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, fileKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                saveFileNames.add(url.toString());
            } catch (SdkClientException e) {
                e.printStackTrace();
            }
        }

        return saveFileNames;
    }

    // MultiFile Image인지 확인
    private boolean isWebp(MultipartFile multipartFile) {
        String[] tokens = multipartFile.getContentType().split("/");
        if(tokens[0].equals("image")){
            throw new RuntimeException("잘못된 파일 타입");
        }
        else if (tokens[1].equals("webp")) {
            throw new RuntimeException("잘못된 이미지 확장자");
        }
        return true;
    }

    // Image가 저장될 Key 생성
    private String getImageKey(MultipartFile file) {
        return UUID.randomUUID() + "-" + file.getOriginalFilename();
    }

    // 버킷 존재 유무 확인 (버킷명)
    private boolean bucketIsExist(String targetBucket) {
        try {
            List<Bucket> buckets = s3.listBuckets();
            for (Bucket bucket : buckets) {
                if (targetBucket.equals(bucket.getName())) {
                    return true;
                }
            }
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
