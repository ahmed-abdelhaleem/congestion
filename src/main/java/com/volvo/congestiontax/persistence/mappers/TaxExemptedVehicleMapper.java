package com.volvo.congestiontax.persistence.mappers;

import com.volvo.congestiontax.core.model.TaxExemptedVehicle;
import com.volvo.congestiontax.core.model.TaxRule;
import com.volvo.congestiontax.persistence.entities.TaxExemptedVehicleEntity;
import com.volvo.congestiontax.persistence.entities.TaxRuleEntity;
import com.volvo.congestiontax.utils.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public abstract class TaxExemptedVehicleMapper{

  @Mapping(target = "sid",expression = MappingUtils.GENERATE_UUID_EXPRESSION)
  public abstract TaxExemptedVehicleEntity map(TaxExemptedVehicle taxRule);

  public abstract TaxExemptedVehicle map(TaxExemptedVehicleEntity taxExemptedVehicleEntity);
}
