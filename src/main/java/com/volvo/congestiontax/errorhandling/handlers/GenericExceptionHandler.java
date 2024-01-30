package com.volvo.congestiontax.errorhandling.handlers;

import com.volvo.congestiontax.errorhandling.dtos.ErrorResponse;
import com.volvo.congestiontax.errorhandling.exceptions.SimpleApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GenericExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleSimpleApiException(SimpleApiException exception, WebRequest webRequest){

    return new ResponseEntity<>(map(exception),exception.getHttpStatus());

  }

  private ErrorResponse map(SimpleApiException exception){

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setErrorCode(exception.getErrorCode());
    errorResponse.setErrorMsg(exception.getErrorMessage());

    return errorResponse;
  }

}
