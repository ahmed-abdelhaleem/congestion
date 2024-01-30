package com.volvo.congestiontax.persistence.repositories;

import com.volvo.congestiontax.persistence.entities.CityTaxConfigEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityTaxConfigRepository extends JpaRepository<CityTaxConfigEntity, Long> {

  List<CityTaxConfigEntity> findAllByCityName(String cityName);
}
