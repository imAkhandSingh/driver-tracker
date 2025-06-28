package com.zuree.driver_tracking.service;

import com.zuree.driver_tracking.model.AlarmImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface AlarmImageService {
    CompletableFuture<AlarmImage> store(MultipartFile file, Long alarmId) throws IOException;
    AlarmImage loadMetadata(Long id);
    Path loadAsPath(AlarmImage img);
}
