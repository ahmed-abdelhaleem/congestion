package com.volvo.congestiontax.persistence.repositories;

import com.volvo.congestiontax.persistence.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

  CityEntity findOneByName(String name);

}
