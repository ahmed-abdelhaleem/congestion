package com.volvo.congestiontax.core.service.persistence;

import com.volvo.congestiontax.core.model.TaxExemptedVehicle;
import com.volvo.congestiontax.core.model.TaxFreeDay;
import java.util.List;
import java.util.Optional;

public interface TaxFreeDayPersistenceService {

  Optional<TaxFreeDay> findOneByDayAndMonthAndCity(short day, short month, String cityName);

  List<TaxFreeDay> findAllByCity(String cityName);

  List<TaxFreeDay> save(List<TaxFreeDay> taxFreeDays);

}
