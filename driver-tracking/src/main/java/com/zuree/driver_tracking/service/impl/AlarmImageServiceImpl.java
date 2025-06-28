package com.zuree.driver_tracking.service.impl;

import com.zuree.driver_tracking.config.FileStorageProperties;
import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.model.AlarmImage;
import com.zuree.driver_tracking.repository.AlarmImageRepository;
import com.zuree.driver_tracking.service.AlarmImageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AlarmImageServiceImpl implements AlarmImageService {

    private final Path root;
    private final AlarmImageRepository repo;

    public AlarmImageServiceImpl(FileStorageProperties props, AlarmImageRepository repo) throws IOException {
        this.root = props.getUploadDir();
        this.repo = repo;
        Files.createDirectories(root);    // ensure folder exists
    }

    @Async
    public CompletableFuture<AlarmImage> store(MultipartFile file, Long alarmId) throws IOException {
        String uuidName = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "");
        Path destination = root.resolve(uuidName).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        AlarmImage img = new AlarmImage();
        img.setAlarm(Alarm.builder().alarmId(alarmId).build());
        img.setFilename(file.getOriginalFilename());
        img.setStoredName(uuidName);
        img.setContentType(file.getContentType());
        img.setSize(file.getSize());
        img.setPath(destination.toString());
        img.setUploadedAt(LocalDateTime.now());

        return CompletableFuture.completedFuture(repo.save(img));
    }

    public AlarmImage loadMetadata(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public Path loadAsPath(AlarmImage img) {
        return Path.of(img.getPath());
    }
}

