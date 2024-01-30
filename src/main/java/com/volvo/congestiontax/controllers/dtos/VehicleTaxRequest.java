package com.volvo.congestiontax.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class VehicleTaxRequest {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private List<LocalDateTime> crossingDateTimes;

  @NotNull
  @JsonProperty("vehicleType")
  private String vehicleType;

  @JsonProperty("city")
  private String city;

}
