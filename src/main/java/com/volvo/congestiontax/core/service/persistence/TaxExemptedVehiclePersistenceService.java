package com.volvo.congestiontax.core.service.persistence;

import com.volvo.congestiontax.core.model.TaxExemptedVehicle;
import java.util.List;
import java.util.Optional;

public interface TaxExemptedVehiclePersistenceService {

  Optional<TaxExemptedVehicle> findOneByVehicleAndCity(String vehicleType, String cityName);

  List<TaxExemptedVehicle> findAllByCity(String cityName);

  List<TaxExemptedVehicle> saveAll(List<TaxExemptedVehicle> taxExemptedVehicles);

}
