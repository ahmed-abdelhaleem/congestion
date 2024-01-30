package com.volvo.congestiontax.persistence.services;

import com.volvo.congestiontax.core.model.TaxFreeDay;
import com.volvo.congestiontax.core.service.persistence.TaxFreeDayPersistenceService;
import com.volvo.congestiontax.persistence.entities.TaxFreeDayEntity;
import com.volvo.congestiontax.persistence.mappers.TaxFreeDayMapper;
import com.volvo.congestiontax.persistence.repositories.TaxFreeDayRepository;
import com.volvo.congestiontax.utils.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxFreeDayPersistenceServiceImpl implements TaxFreeDayPersistenceService {

  private final TaxFreeDayRepository taxFreeDayRepository;

  private final TaxFreeDayMapper taxFreeDayMapper;

  @Override
  public Optional<TaxFreeDay> findOneByDayAndMonthAndCity(short day, short month, String cityName) {

    TaxFreeDayEntity taxFreeDayEntity = taxFreeDayRepository.findOneByDayAndMonthAndCityName(day, month, cityName);

    return Optional.ofNullable(taxFreeDayEntity)
        .map(taxFreeDayMapper::map);
  }

  @Override
  public List<TaxFreeDay> findAllByCity(String cityName) {

    var taxFreeDays = taxFreeDayRepository.findAllByCityName(cityName);

    return CollectionUtils.safeStream(taxFreeDays)
        .map(taxFreeDayMapper::map)
        .collect(Collectors.toList());
  }

  @Override
  public List<TaxFreeDay> save(List<TaxFreeDay> taxFreeDays) {

    if (CollectionUtils.isEmpty(taxFreeDays)) {
      return Collections.emptyList();
    }

    var taxFreeDaysEntities = CollectionUtils.safeStream(taxFreeDays)
        .map(taxFreeDayMapper::map)
        .collect(Collectors.toList());

    taxFreeDaysEntities = taxFreeDayRepository.saveAll(taxFreeDaysEntities);

    return CollectionUtils.safeStream(taxFreeDaysEntities)
        .map(taxFreeDayMapper::map)
        .collect(Collectors.toList());
  }
}
