package com.volvo.congestiontax.persistence.services;

import com.volvo.congestiontax.core.model.TaxExemptedVehicle;
import com.volvo.congestiontax.core.service.persistence.TaxExemptedVehiclePersistenceService;
import com.volvo.congestiontax.persistence.mappers.TaxExemptedVehicleMapper;
import com.volvo.congestiontax.persistence.repositories.TaxExemptedVehicleRepository;
import com.volvo.congestiontax.utils.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxExemptedVehiclePersistenceServiceImpl implements TaxExemptedVehiclePersistenceService {

  private final TaxExemptedVehicleRepository taxExemptedVehicleRepository;

  private final TaxExemptedVehicleMapper taxExemptedVehicleMapper;

  @Override
  public Optional<TaxExemptedVehicle> findOneByVehicleAndCity(String vehicleType, String cityName) {

    var taxExemptedVehicleEntity = taxExemptedVehicleRepository.findOneByVehicleTypeAndCityName(vehicleType, cityName);

    return Optional.ofNullable(taxExemptedVehicleEntity)
        .map(taxExemptedVehicleMapper::map);
  }

  @Override
  public List<TaxExemptedVehicle> findAllByCity(String cityName) {

    var taxExemptedVehicleEntities = taxExemptedVehicleRepository.findAllByCityName(cityName);

    return CollectionUtils.safeStream(taxExemptedVehicleEntities)
        .map(taxExemptedVehicleMapper::map)
        .collect(Collectors.toList());
  }

  @Override
  public List<TaxExemptedVehicle> saveAll(List<TaxExemptedVehicle> taxExemptedVehicles) {

    if (CollectionUtils.isEmpty(taxExemptedVehicles)) {
      return Collections.emptyList();
    }

    var taxExemptedVehicleEntities = CollectionUtils.safeStream(taxExemptedVehicles)
        .map(taxExemptedVehicleMapper::map)
        .collect(Collectors.toList());

    taxExemptedVehicleEntities = taxExemptedVehicleRepository.saveAll(taxExemptedVehicleEntities);

    return CollectionUtils.safeStream(taxExemptedVehicleEntities)
        .map(taxExemptedVehicleMapper::map)
        .collect(Collectors.toList());
  }
}
