package com.volvo.congestiontax.core.service;

import com.volvo.congestiontax.errorhandling.exceptions.SimpleApiException;
import java.time.LocalDateTime;
import java.util.List;

public interface CongestionTaxService {

  double getCongestionTax(String vehicleType, List<LocalDateTime> crossingDateTimes, String city)
      throws SimpleApiException;

}
