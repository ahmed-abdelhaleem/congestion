package com.volvo.congestiontax.persistence.repositories;

import com.volvo.congestiontax.persistence.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

  VehicleEntity findOneByType(String type);
}
