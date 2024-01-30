package com.volvo.congestiontax.core.service;

import com.volvo.congestiontax.core.model.CityTaxConfig;
import com.volvo.congestiontax.core.model.TaxExemptedVehicle;
import com.volvo.congestiontax.core.model.TaxFreeDay;
import com.volvo.congestiontax.core.model.TaxRule;
import com.volvo.congestiontax.core.model.Vehicle;
import com.volvo.congestiontax.core.service.persistence.CityTaxConfigPersistenceService;
import com.volvo.congestiontax.core.service.persistence.TaxExemptedVehiclePersistenceService;
import com.volvo.congestiontax.core.service.persistence.TaxFreeDayPersistenceService;
import com.volvo.congestiontax.core.service.persistence.TaxRulePersistenceService;
import com.volvo.congestiontax.errorhandling.ErrorCodesAndMessages;
import com.volvo.congestiontax.errorhandling.exceptions.SimpleApiException;
import com.volvo.congestiontax.utils.CollectionUtils;
import com.volvo.congestiontax.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CongestionTaxServiceImpl implements CongestionTaxService {

  public static final String ONE_CHARGE_WITHIN_MINUTES_KEY = "one_charge_per_minutes";
  public static final String WEEKEND_TAX_FREE_ENABLED_KEY = "weekend_tax_free_enabled";
  public static final String TAX_FREE_DAYS_BEFORE_HOLIDAY_ENABLED = "tax_free_days_before_holiday_enabled";
  public static final String TAX_FREE_DAYS_BEFORE_HOLIDAY_NUMBER = "tax_free_days_before_holiday_number";

  private final TaxExemptedVehiclePersistenceService taxExemptedVehiclePersistenceService;

  private final TaxFreeDayPersistenceService taxFreeDayPersistenceService;

  private final TaxRulePersistenceService taxRulePersistenceService;

  private final CityTaxConfigPersistenceService cityTaxConfigPersistenceService;

  @Override
  public double getCongestionTax(String vehicleType, List<LocalDateTime> crossingDateTimes, String city)
      throws SimpleApiException {

    var exemptedVehicles = taxExemptedVehiclePersistenceService.findAllByCity(city);
    var exemptedVehiclesSize = ObjectUtils.isEmpty(exemptedVehicles) ? 0 : exemptedVehicles.size();
    log.info("retrieved {} exempted vehicle types for city {}", exemptedVehiclesSize, city);

    var activeTaxExemptedVehicles = CollectionUtils.safeStream(exemptedVehicles)
        .filter(TaxExemptedVehicle::isActive)
        .map(TaxExemptedVehicle::getVehicle)
        .map(Vehicle::getType)
        .map(String::toLowerCase)
        .collect(Collectors.toList());
    var activeExemptedVehiclesSize =
        ObjectUtils.isEmpty(activeTaxExemptedVehicles) ? 0 : activeTaxExemptedVehicles.size();
    log.info("while active exempted vehicle are {} ", activeExemptedVehiclesSize, city);

    if (activeTaxExemptedVehicles.contains(vehicleType.toLowerCase())) {
      log.info("Vehicle {} is tax exempted in {} city", vehicleType, city);
      return 0.0d;
    }

    var cityTaxConfigs = cityTaxConfigPersistenceService.findAllByCity(city);

    var taxFreeDays = taxFreeDayPersistenceService.findAllByCity(city);
    var taxFreeDaysSize = ObjectUtils.isEmpty(taxFreeDays) ? 0 : taxFreeDays.size();
    log.info("retrieved {} tax free days for city {}", taxFreeDaysSize, city);

    var activeTaxFreeDays = CollectionUtils.safeStream(taxFreeDays)
        .filter(TaxFreeDay::isActive)
        .collect(Collectors.toList());
    var activeTaxFreeDaysSize = ObjectUtils.isEmpty(activeTaxFreeDays) ? 0 : activeTaxFreeDays.size();
    log.info("while active tax free days are {} ", activeTaxFreeDaysSize, city);

    crossingDateTimes = excludeWeekendDates(crossingDateTimes, cityTaxConfigs);
    crossingDateTimes = excludeFreeDays(crossingDateTimes, taxFreeDays);

    var holidayFreeTaxDays = CollectionUtils.safeStream(activeTaxFreeDays)
        .filter(TaxFreeDay::isHoliday)
        .collect(Collectors.toList());
    var holidayFreeTaxDaysSize = ObjectUtils.isEmpty(holidayFreeTaxDays) ? 0 : holidayFreeTaxDays.size();
    log.info("And the active holiday tax free days are {} ", holidayFreeTaxDaysSize, city);
    crossingDateTimes = excludeBeforeHolidays(crossingDateTimes, cityTaxConfigs, holidayFreeTaxDays);

    if (crossingDateTimes.isEmpty()) {
      return 0.0;
    }

    var taxRules = taxRulePersistenceService.findAllByCity(city);

    return calculateTaxPerRules(crossingDateTimes, taxRules, cityTaxConfigs);

  }

  private double calculateTaxPerRules(List<LocalDateTime> crossingDateTimes,
      List<TaxRule> taxRules,
      List<CityTaxConfig> cityTaxConfigs) throws SimpleApiException {

    double totalCongestionTax = 0.0;

    int oneChargePerMinutes = CollectionUtils.safeStream(cityTaxConfigs)
        .filter(config -> ONE_CHARGE_WITHIN_MINUTES_KEY.equals(config.getConfigKey()))
        .map(CityTaxConfig::getConfigValue)
        .map(Integer::parseInt)
        .findAny()
        .orElseThrow(() -> getConfigKeyErrorException());

    Map<LocalDateTime, Double> crossingTimesWithTaxCharges = CollectionUtils.safeStream(crossingDateTimes)
        .flatMap(crossingDateTime -> {

          var crossingLocalTime = crossingDateTime.toLocalTime();
          return CollectionUtils.safeStream(taxRules)
              .filter(taxRule -> {

                if (taxRule.getStartTime().isAfter(taxRule.getEndTime())) {
                  return !crossingLocalTime.isBefore(taxRule.getStartTime().toLocalTime());
                }
                return !crossingLocalTime.isBefore(taxRule.getStartTime().toLocalTime()) &&
                    !crossingLocalTime.isAfter(taxRule.getEndTime().plusSeconds(59).toLocalTime());
              })
              .map(TaxRule::getTaxAmount)
              .map(taxAmount -> new SimpleEntry<>(crossingDateTime, taxAmount));
        }).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    List<LocalDateTime> sortedCrossingTimes = crossingTimesWithTaxCharges.keySet().stream().sorted().toList();

    for (int i = 0; i < sortedCrossingTimes.size(); i++) {

      LocalDateTime localDateTime = sortedCrossingTimes.get(i);
      double tempHighTax = crossingTimesWithTaxCharges.get(localDateTime);

      LocalDateTime maxedLocalDateTime = localDateTime.plusMinutes(oneChargePerMinutes);

      for (int j = i + 1; j < sortedCrossingTimes.size(); j++) {

        LocalDateTime nextLocalDateTime = sortedCrossingTimes.get(j);

        if (nextLocalDateTime.isBefore(maxedLocalDateTime)) {

          double nextTaxCharge = crossingTimesWithTaxCharges.get(nextLocalDateTime);

          if (nextTaxCharge > tempHighTax) {
            tempHighTax = nextTaxCharge;
          }
          i = j;
        } else {
          break;
        }
      }
      totalCongestionTax += tempHighTax;

    }

    return totalCongestionTax;

  }

  private List<LocalDateTime> excludeWeekendDates(List<LocalDateTime> localDateTimes,
      List<CityTaxConfig> cityTaxConfigs)
      throws SimpleApiException {

    boolean weekendTaxFreeEnabled = CollectionUtils.safeStream(cityTaxConfigs)
        .filter(config -> WEEKEND_TAX_FREE_ENABLED_KEY.equals(config.getConfigKey()))
        .map(CityTaxConfig::getConfigValue)
        .map(Boolean::parseBoolean)
        .findAny()
        .orElseThrow(() -> getConfigKeyErrorException());

    if (!weekendTaxFreeEnabled) {
      return localDateTimes;
    }

    return CollectionUtils.safeStream(localDateTimes)
        .filter(ldt -> !DateUtils.isWeekend(ldt))
        .collect(Collectors.toList());
  }

  private List<LocalDateTime> excludeBeforeHolidays(List<LocalDateTime> localDateTimes,
      List<CityTaxConfig> cityTaxConfigs, List<TaxFreeDay> holidayFreeTaxDays) throws SimpleApiException {

    boolean taxFreeBeforeHoliday = CollectionUtils.safeStream(cityTaxConfigs)
        .filter(config -> TAX_FREE_DAYS_BEFORE_HOLIDAY_ENABLED.equals(config.getConfigKey()))
        .map(CityTaxConfig::getConfigValue)
        .map(Boolean::parseBoolean)
        .findAny()
        .orElseThrow(() -> getConfigKeyErrorException());

    if (!taxFreeBeforeHoliday) {
      return localDateTimes;
    }

    int taxFreeBeforeHolidayNumber = CollectionUtils.safeStream(cityTaxConfigs)
        .filter(config -> TAX_FREE_DAYS_BEFORE_HOLIDAY_NUMBER.equals(config.getConfigKey()))
        .map(CityTaxConfig::getConfigValue)
        .map(Integer::parseInt)
        .findAny()
        .orElseThrow(() -> getConfigKeyErrorException());

    return CollectionUtils.safeStream(localDateTimes)
        .filter(localDateTime -> CollectionUtils.safeStream(holidayFreeTaxDays)
            .noneMatch(taxFreeDay -> {
              LocalDateTime atHolidayLocalDateTime = LocalDateTime.of(localDateTime.getYear(), taxFreeDay.getMonth(),
                  taxFreeDay.getDay(), 0, 0);
              LocalDateTime beforeHolidayLocalDateTime = LocalDateTime.of(localDateTime.getYear(), taxFreeDay.getMonth(),
                  taxFreeDay.getDay(), 0, 0);
              beforeHolidayLocalDateTime = beforeHolidayLocalDateTime.minusDays(taxFreeBeforeHolidayNumber);

              return localDateTime.isAfter(beforeHolidayLocalDateTime) && localDateTime.isBefore(atHolidayLocalDateTime);
            }))
        .collect(Collectors.toList());
  }

  private List<LocalDateTime> excludeFreeDays(List<LocalDateTime> localDateTimes, List<TaxFreeDay> taxFreeDays) {

    return CollectionUtils.safeStream(localDateTimes)
        .filter(localDateTime -> {
          var day = localDateTime.getDayOfMonth();
          var month = localDateTime.getMonth().getValue();

          return CollectionUtils.safeStream(taxFreeDays)
              .noneMatch(taxFreeDay -> taxFreeDay.getDay() == day && taxFreeDay.getMonth() == month);
        })
        .collect(Collectors.toList());
  }

  private SimpleApiException getConfigKeyErrorException() {

    return new SimpleApiException(ErrorCodesAndMessages.CONFIG_KEY_NOT_FOUND_CODE,
        ErrorCodesAndMessages.CONFIG_KEY_NOT_FOUND_MSG,
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
