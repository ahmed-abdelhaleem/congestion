package com.volvo.congestiontax.persistence.mappers;

import com.volvo.congestiontax.core.model.Vehicle;
import com.volvo.congestiontax.persistence.entities.VehicleEntity;
import com.volvo.congestiontax.utils.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public abstract class VehicleMapper {

  @Mapping(target = "sid", expression = MappingUtils.GENERATE_UUID_EXPRESSION)
  public abstract VehicleEntity map(Vehicle vehicle);

  public abstract Vehicle map(VehicleEntity vehicleEntity);
}
