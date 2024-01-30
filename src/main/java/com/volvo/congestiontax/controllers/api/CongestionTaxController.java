package com.volvo.congestiontax.controllers.api;

import com.volvo.congestiontax.controllers.dtos.VehicleTaxRequest;
import com.volvo.congestiontax.controllers.dtos.VehicleTaxResponse;
import com.volvo.congestiontax.core.service.CongestionTaxService;
import com.volvo.congestiontax.errorhandling.exceptions.SimpleApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/congestion-tax")
public class CongestionTaxController {

  private final CongestionTaxService congestionTaxService;

  @PostMapping
  public ResponseEntity<VehicleTaxResponse> getTaxCostForVehicle(@RequestBody VehicleTaxRequest vehicleTaxRequest)
      throws SimpleApiException {

    double congestionTax = congestionTaxService.getCongestionTax(vehicleTaxRequest.getVehicleType(),
        vehicleTaxRequest.getCrossingDateTimes(), vehicleTaxRequest.getCity());

    return new ResponseEntity<>(new VehicleTaxResponse(congestionTax), HttpStatus.OK);
  }
}
