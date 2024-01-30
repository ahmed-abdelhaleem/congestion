package com.volvo.congestiontax.persistence.repositories;

import com.volvo.congestiontax.persistence.entities.TaxExemptedVehicleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxExemptedVehicleRepository extends JpaRepository<TaxExemptedVehicleEntity, Long> {

  TaxExemptedVehicleEntity findOneByVehicleTypeAndCityName(String vehicleType, String cityName);

  List<TaxExemptedVehicleEntity> findAllByCityName(String cityName);

}
