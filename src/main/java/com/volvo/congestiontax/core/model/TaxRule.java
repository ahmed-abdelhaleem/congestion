package com.volvo.congestiontax.core.model;

import java.time.OffsetTime;
import lombok.Data;

@Data
public class TaxRule {

  private String sid;

  private OffsetTime startTime;

  private OffsetTime endTime;

  private Double taxAmount;

  private City city;

}
