package com.volvo.congestiontax.core.model;

import lombok.Data;

@Data
public class CityTaxConfig {

  private String sid;

  private String configKey;

  private String configValue;

  private City city;

}
