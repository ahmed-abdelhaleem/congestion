package com.volvo.congestiontax.core.service.persistence;

import com.volvo.congestiontax.core.model.City;
import com.volvo.congestiontax.core.model.TaxRule;
import java.time.OffsetTime;
import java.util.List;

public interface TaxRulePersistenceService {

  List<TaxRule> findAllByCity(String cityName);

  List<TaxRule> saveAll(List<TaxRule> taxRules);

}
