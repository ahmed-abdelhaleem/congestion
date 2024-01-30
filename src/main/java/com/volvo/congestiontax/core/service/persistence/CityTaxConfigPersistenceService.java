package com.volvo.congestiontax.core.service.persistence;

import com.volvo.congestiontax.core.model.CityTaxConfig;
import java.util.List;

public interface CityTaxConfigPersistenceService {

  List<CityTaxConfig> findAllByCity(String city);

  List<CityTaxConfig> saveAll(List<CityTaxConfig> cityTaxConfigs);

}
