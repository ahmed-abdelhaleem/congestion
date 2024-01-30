package com.volvo.congestiontax.utils;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MappingUtils {

  public static final String GENERATE_UUID_EXPRESSION = "java(com.volvo.congestiontax.utils.MappingUtils.generateUuid())";

  public static String generateUuid() {
    return UUID.randomUUID().toString();
  }

}
