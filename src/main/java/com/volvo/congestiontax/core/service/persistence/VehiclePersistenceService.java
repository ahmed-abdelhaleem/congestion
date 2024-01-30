package com.volvo.congestiontax.core.service.persistence;

import com.volvo.congestiontax.core.model.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehiclePersistenceService {

  Optional<Vehicle> findOneByType(String type);

  List<Vehicle> findAll();

  List<Vehicle> saveAll(List<Vehicle> vehicles);

}
