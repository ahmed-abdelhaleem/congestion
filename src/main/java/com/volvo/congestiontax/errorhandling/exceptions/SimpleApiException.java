package com.volvo.congestiontax.errorhandling.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleApiException extends Exception{

  private String errorCode;
  private String errorMessage;
  private HttpStatus httpStatus;
}
