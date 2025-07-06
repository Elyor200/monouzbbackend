package com.monouzbekistanbackend.dto.image;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ImageUploadRequest(
        MultipartFile file,
        UUID productId
) {}
