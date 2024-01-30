package com.volvo.congestiontax.persistence.repositories;

import com.volvo.congestiontax.persistence.entities.TaxRuleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRuleRepository extends JpaRepository<TaxRuleEntity, Long> {

  List<TaxRuleEntity> findByCityName(String cityName);

}
