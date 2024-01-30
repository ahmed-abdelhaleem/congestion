package com.volvo.congestiontax.core.model;

import lombok.Data;

@Data
public class TaxFreeDay {

  private String sid;

  private short day;

  private short month;

  private boolean active;

  private boolean holiday;

  private City city;
}
