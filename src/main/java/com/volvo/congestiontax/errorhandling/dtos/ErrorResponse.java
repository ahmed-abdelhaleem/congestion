package com.volvo.congestiontax.errorhandling.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {

  @JsonProperty("errorCode")
  private String errorCode;

  @JsonProperty("errorMessage")
  private String errorMsg;
}
