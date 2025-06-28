package com.zuree.driver_tracking.repository;

import com.zuree.driver_tracking.model.AlarmImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmImageRepository extends JpaRepository<AlarmImage, Long> {
}
