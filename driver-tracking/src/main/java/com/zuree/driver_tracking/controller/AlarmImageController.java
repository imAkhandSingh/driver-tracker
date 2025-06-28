package com.zuree.driver_tracking.controller;

import com.zuree.driver_tracking.dto.response.ImageResponse;
import com.zuree.driver_tracking.dto.response.SuccessResponse;
import com.zuree.driver_tracking.model.AlarmImage;
import com.zuree.driver_tracking.service.AlarmImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/image/${api.version}")
@RequiredArgsConstructor
public class AlarmImageController {

    private final AlarmImageService service;

    @Value("${preview-url:/api/images/preview/}")
    private String previewBase;

    @Value("${download-url:/api/images/download/}")
    private String downloadBase;

    /** POST (multipart/form-data “file”) */
    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<SuccessResponse<ImageResponse>>> upload(@RequestParam("file") MultipartFile file, @RequestParam("alarmId") Long alarmId) throws Exception {
        return service.store(file, alarmId)
                .thenApply(img -> ResponseEntity.ok(new SuccessResponse<>(new ImageResponse(img, previewBase, downloadBase))));
    }

    /** GET display in browser */
    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> view(@PathVariable Long id) {
        AlarmImage img = service.loadMetadata(id);
        FileSystemResource resource = new FileSystemResource(service.loadAsPath(img));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getContentType()))
                .contentLength(img.getSize())
                .body(resource);
    }

    /** GET force download */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        AlarmImage img = service.loadMetadata(id);
        Resource res = new FileSystemResource(service.loadAsPath(img));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + img.getFilename() + "\"")
                .body(res);
    }

}

