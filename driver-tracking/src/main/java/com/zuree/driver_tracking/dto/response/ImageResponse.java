package com.zuree.driver_tracking.dto.response;

import com.zuree.driver_tracking.model.AlarmImage;

public record ImageResponse(Long id, String originalName, String storedName, Long size,
                     String contentType, String previewUrl,
                     String downloadUrl) {
    public ImageResponse(AlarmImage img, String previewBase, String downloadBase) {
        this(img.getImageId(), img.getFilename(), img.getStoredName(), img.getSize(),
                img.getContentType(),
                previewBase + img.getImageId(),
                downloadBase + img.getImageId());
    }
}
