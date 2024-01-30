package com.volvo.congestiontax.persistence.services;

import com.volvo.congestiontax.core.model.TaxRule;
import com.volvo.congestiontax.core.service.persistence.TaxRulePersistenceService;
import com.volvo.congestiontax.persistence.mappers.TaxRuleMapper;
import com.volvo.congestiontax.persistence.repositories.TaxRuleRepository;
import com.volvo.congestiontax.utils.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxRulePersistenceServiceImpl implements TaxRulePersistenceService {

  private final TaxRuleRepository taxRuleRepository;

  private final TaxRuleMapper taxRuleMapper;

  @Override
  public List<TaxRule> findAllByCity(String cityName) {

    var taxRuleEntities = taxRuleRepository.findByCityName(cityName);

    return CollectionUtils.safeStream(taxRuleEntities)
        .map(taxRuleMapper::map)
        .collect(Collectors.toList());
  }

  @Override
  public List<TaxRule> saveAll(List<TaxRule> taxRules) {

    if (CollectionUtils.isEmpty(taxRules)) {
      return Collections.emptyList();
    }

    var taxRuleEntities = CollectionUtils.safeStream(taxRules)
        .map(taxRuleMapper::map)
        .collect(Collectors.toList());

    taxRuleEntities = taxRuleRepository.saveAll(taxRuleEntities);

    return CollectionUtils.safeStream(taxRuleEntities)
        .map(taxRuleMapper::map)
        .collect(Collectors.toList());
  }

}
