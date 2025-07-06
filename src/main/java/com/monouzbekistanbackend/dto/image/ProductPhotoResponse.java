package com.monouzbekistanbackend.dto.image;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public record ProductPhotoResponse(UUID imageId, String url, String color, boolean main) {}
