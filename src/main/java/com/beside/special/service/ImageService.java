package com.beside.special.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.beside.special.domain.Image;
import com.beside.special.domain.ImageRepository;

import com.beside.special.exception.BadRequestException;
import com.beside.special.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private static final String BUCKET_NAME = "team.special";

    private final AmazonS3 s3;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, AmazonS3 s3) {
        this.imageRepository = imageRepository;
        this.s3 = s3;
    }

    // 이미지 업로드 ( MultiPartFile, 저장 디렉토리 ) TODO File Size 처리 (제한?)
    public List<String> uploadImage(List<MultipartFile> images, String targetDirectory) {
        List<String> imageUuids = new ArrayList<>();

        for (MultipartFile file : images) {
            validateImage(file);

            String fileKey = targetDirectory + "/" + getImageKey(file);
            try {
                s3.putObject(new PutObjectRequest(
                        BUCKET_NAME,
                        fileKey,
                        file.getInputStream(),
                        new ObjectMetadata()));

            } catch (SdkClientException | IOException e) {
                throw new RuntimeException("S3 Upload 중 오류 발생" + e.getMessage());
            }

            Image image = Image.builder()
                    .fileKey(fileKey)
                    .uuid(String.valueOf(UUID.randomUUID()))
                    .build();

            imageRepository.save(image);

            imageUuids.add(image.getUuid());
        }
        return imageUuids;
    }

    public ByteArrayOutputStream getImage(String uuid) {
        Image image = imageRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 파일입니다."));

        S3Object s3Object = s3.getObject(BUCKET_NAME, image.getFileKey());

        try (InputStream is = s3Object.getObjectContent();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            int read_len = 0;
            byte[] read_buffer = new byte[4096];
            while ((read_len = is.read(read_buffer)) > 0) {
                outputStream.write(read_buffer, 0, read_len);
            }
            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException("존재하지 않는 파일입니다.");
        }
    }

    public void deleteImage(List<String> uuids) {
        for (String uuid : uuids) {
            Image image = imageRepository.findByUuid(uuid)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 파일입니다."));

            s3.deleteObject(BUCKET_NAME, image.getFileKey());
        }
    }

    // MultiFile Image 검증
    private void validateImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BadRequestException("빈 파일입니다.");
        }
        String[] tokens = multipartFile.getContentType().split("/");
        if (!tokens[0].equals("image")) {
            throw new BadRequestException("잘못된 파일 타입입니다.");
        }
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
            throw new RuntimeException(e.getMessage());
        }
        return false;
    }
}
