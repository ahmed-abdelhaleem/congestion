package com.volvo.congestiontax.persistence.services;

import com.volvo.congestiontax.core.model.Vehicle;
import com.volvo.congestiontax.core.service.persistence.VehiclePersistenceService;
import com.volvo.congestiontax.persistence.entities.VehicleEntity;
import com.volvo.congestiontax.persistence.mappers.VehicleMapper;
import com.volvo.congestiontax.persistence.repositories.VehicleRepository;
import com.volvo.congestiontax.utils.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiclePersistenceServiceImpl implements VehiclePersistenceService {

  private final VehicleRepository vehicleRepository;

  private final VehicleMapper vehicleMapper;

  @Override
  public Optional<Vehicle> findOneByType(String type) {

    VehicleEntity vehicleEntity = vehicleRepository.findOneByType(type);

    return Optional.ofNullable(vehicleEntity).map(vehicleMapper::map);
  }

  @Override
  public List<Vehicle> saveAll(List<Vehicle> vehicles) {

    if (CollectionUtils.isEmpty(vehicles)) {
      return Collections.emptyList();
    }

    var vehiclesEntities = CollectionUtils.safeStream(vehicles)
        .map(vehicleMapper::map)
        .collect(Collectors.toList());

    vehiclesEntities = vehicleRepository.saveAll(vehiclesEntities);

    return CollectionUtils.safeStream(vehiclesEntities)
        .map(vehicleMapper::map)
        .collect(Collectors.toList());
  }

  @Override
  public List<Vehicle> findAll() {

    return CollectionUtils.safeStream(vehicleRepository.findAll())
        .map(vehicleMapper::map)
        .collect(Collectors.toList());
  }

}
