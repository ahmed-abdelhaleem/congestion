package com.volvo.congestiontax.utils;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

  public static boolean isWeekend(LocalDateTime localDateTime) {

    switch (localDateTime.getDayOfWeek()) {
      case SATURDAY:
      case SUNDAY: {
        return true;
      }
      default: {
        return false;
      }
    }
  }

}
