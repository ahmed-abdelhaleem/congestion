package com.volvo.congestiontax.persistence.services;


import com.volvo.congestiontax.core.model.CityTaxConfig;
import com.volvo.congestiontax.core.service.persistence.CityTaxConfigPersistenceService;
import com.volvo.congestiontax.persistence.mappers.CityTaxConfigMapper;
import com.volvo.congestiontax.persistence.repositories.CityTaxConfigRepository;
import com.volvo.congestiontax.utils.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityTaxConfigPersistenceServiceImpl implements CityTaxConfigPersistenceService {

  private final CityTaxConfigRepository cityTaxConfigRepository;

  private final CityTaxConfigMapper cityTaxConfigMapper;

  @Override
  public List<CityTaxConfig> findAllByCity(String city) {

    var cityTaxConfigs = cityTaxConfigRepository.findAllByCityName(city);

    return CollectionUtils.safeStream(cityTaxConfigs)
        .map(cityTaxConfigMapper::map)
        .collect(Collectors.toList());
  }

  @Override
  public List<CityTaxConfig> saveAll(List<CityTaxConfig> cityTaxConfigs) {

    if (CollectionUtils.isEmpty(cityTaxConfigs)) {
      return Collections.emptyList();
    }

    var cityTaxConfigEntities = CollectionUtils.safeStream(cityTaxConfigs)
        .map(cityTaxConfigMapper::map)
        .collect(Collectors.toList());

    cityTaxConfigEntities = cityTaxConfigRepository.saveAll(cityTaxConfigEntities);

    return CollectionUtils.safeStream(cityTaxConfigEntities)
        .map(cityTaxConfigMapper::map)
        .collect(Collectors.toList());
  }
}
