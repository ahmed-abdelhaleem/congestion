package com.volvo.congestiontax.persistence.mappers;

import com.volvo.congestiontax.core.model.City;
import com.volvo.congestiontax.persistence.entities.CityEntity;
import com.volvo.congestiontax.utils.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public abstract class CityMapper {

  @Mapping(target = "sid", expression = MappingUtils.GENERATE_UUID_EXPRESSION)
  public abstract CityEntity map(City city);

  public abstract City map(CityEntity cityEntity);

}
