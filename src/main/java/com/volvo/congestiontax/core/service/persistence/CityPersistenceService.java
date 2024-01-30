package com.volvo.congestiontax.core.service.persistence;

import com.volvo.congestiontax.core.model.City;
import java.util.List;
import java.util.Optional;

public interface CityPersistenceService {

  Optional<City> findOneByName(String name);

  List<City> findAll();

  List<City> saveAll(List<City> cities);

}
