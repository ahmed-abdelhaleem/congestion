package com.volvo.congestiontax.persistence.mappers;

import com.volvo.congestiontax.core.model.TaxFreeDay;
import com.volvo.congestiontax.persistence.entities.TaxFreeDayEntity;
import com.volvo.congestiontax.utils.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.WARN)
public abstract class TaxFreeDayMapper {

  @Mapping(target = "sid", expression = MappingUtils.GENERATE_UUID_EXPRESSION)
  public abstract TaxFreeDayEntity map(TaxFreeDay taxRule);

  public abstract TaxFreeDay map(TaxFreeDayEntity taxFreeDayEntity);
}
