package com.volvo.congestiontax.persistence.mappers;

import com.volvo.congestiontax.core.model.CityTaxConfig;
import com.volvo.congestiontax.persistence.entities.CityTaxConfigEntity;
import com.volvo.congestiontax.utils.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public abstract class CityTaxConfigMapper {

  @Mapping(target = "sid", expression = MappingUtils.GENERATE_UUID_EXPRESSION)
  public abstract CityTaxConfigEntity map(CityTaxConfig cityTaxConfig);

  public abstract CityTaxConfig map(CityTaxConfigEntity cityTaxConfigEntity);

}
