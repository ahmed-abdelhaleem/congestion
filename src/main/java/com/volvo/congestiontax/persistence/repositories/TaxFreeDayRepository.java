package com.volvo.congestiontax.persistence.repositories;

import com.volvo.congestiontax.persistence.entities.TaxFreeDayEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxFreeDayRepository extends JpaRepository<TaxFreeDayEntity, Long> {

  TaxFreeDayEntity findOneByDayAndMonthAndCityName(short day, short month, String cityName);

  List<TaxFreeDayEntity> findAllByCityName(String cityName);
}
