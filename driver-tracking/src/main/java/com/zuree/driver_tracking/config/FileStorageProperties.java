package com.zuree.driver_tracking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Path;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.file")
public class FileStorageProperties {
    private Path uploadDir = Path.of("uploads");
}

