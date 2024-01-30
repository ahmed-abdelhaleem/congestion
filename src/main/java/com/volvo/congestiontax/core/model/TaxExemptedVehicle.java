package com.volvo.congestiontax.core.model;

import lombok.Data;

@Data
public class TaxExemptedVehicle {

  private String sid;

  private boolean active;

  private Vehicle vehicle;

  private City city;

}
