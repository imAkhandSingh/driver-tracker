package com.zuree.driver_tracking.repository;

import com.zuree.driver_tracking.dto.AlarmManagerDTO;
import com.zuree.driver_tracking.model.Alarm;
import com.zuree.driver_tracking.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByDevice(Device id, Pageable pageable);
    @Query("""
    SELECT a FROM Alarm a
    WHERE a.device.manager.id = :managerId
    """)
    Page<Alarm> findByManagerId(@Param("managerId") Long managerId, Pageable pageable);
}
