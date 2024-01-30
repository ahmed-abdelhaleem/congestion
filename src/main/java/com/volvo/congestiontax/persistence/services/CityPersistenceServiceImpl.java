package com.volvo.congestiontax.persistence.services;

import com.volvo.congestiontax.core.model.City;
import com.volvo.congestiontax.core.service.persistence.CityPersistenceService;
import com.volvo.congestiontax.persistence.entities.CityEntity;
import com.volvo.congestiontax.persistence.mappers.CityMapper;
import com.volvo.congestiontax.persistence.repositories.CityRepository;
import com.volvo.congestiontax.utils.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityPersistenceServiceImpl implements CityPersistenceService {

  private final CityRepository cityRepository;

  private final CityMapper cityMapper;

  @Override
  public Optional<City> findOneByName(String name) {

    CityEntity city = cityRepository.findOneByName(name);

    return Optional.ofNullable(city).map(cityMapper::map);

  }

  @Override
  public List<City> findAll() {

    return CollectionUtils.safeStream(cityRepository.findAll())
        .map(cityMapper::map)
        .collect(Collectors.toList());
  }

  @Override
  public List<City> saveAll(List<City> cities) {

    if (CollectionUtils.isEmpty(cities)) {
      return Collections.emptyList();
    }

    var citiesEntities = CollectionUtils.safeStream(cities)
        .map(cityMapper::map)
        .collect(Collectors.toList());

    citiesEntities = cityRepository.saveAll(citiesEntities);

    return CollectionUtils.safeStream(citiesEntities)
        .map(cityMapper::map)
        .collect(Collectors.toList());

  }
}
