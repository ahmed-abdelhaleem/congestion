package com.volvo.congestiontax.core.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import com.volvo.congestiontax.core.model.City;
import com.volvo.congestiontax.core.model.CityTaxConfig;
import com.volvo.congestiontax.core.model.TaxExemptedVehicle;
import com.volvo.congestiontax.core.model.TaxFreeDay;
import com.volvo.congestiontax.core.model.TaxRule;
import com.volvo.congestiontax.core.model.Vehicle;
import com.volvo.congestiontax.core.service.CongestionTaxServiceImpl;
import com.volvo.congestiontax.core.service.persistence.CityTaxConfigPersistenceService;
import com.volvo.congestiontax.core.service.persistence.TaxExemptedVehiclePersistenceService;
import com.volvo.congestiontax.core.service.persistence.TaxFreeDayPersistenceService;
import com.volvo.congestiontax.core.service.persistence.TaxRulePersistenceService;
import com.volvo.congestiontax.errorhandling.exceptions.SimpleApiException;
import java.time.LocalDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CongestionTaxServiceImplTest {

  private static final String CITY = "Gothenburg";
  @Mock
  private TaxExemptedVehiclePersistenceService taxExemptedVehiclePersistenceService;

  @Mock
  private TaxFreeDayPersistenceService taxFreeDayPersistenceService;

  @Mock
  private TaxRulePersistenceService taxRulePersistenceService;

  @Mock
  private CityTaxConfigPersistenceService cityTaxConfigPersistenceService;

  @InjectMocks
  private CongestionTaxServiceImpl congestionTaxService;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  @DisplayName("Exempted vehicles ")
  @MethodSource("allExemptedVehicles")
  @ParameterizedTest(name = "with value:{0}")
  void getCongestionTax_emergency_expectZero(String vehicleType) throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2013, 6, 4, 6, 0);

    var crossingTimes = List.of(localDateTime_1);

    double congestionTax = congestionTaxService.getCongestionTax(vehicleType, crossingTimes, CITY);

    assertEquals(0.0, congestionTax);
  }

  @Test
  void getCongestionTax_twoWithin60AndOneAtNightRestInJuly_expectTaxForOne() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    doReturn(List.of(getActiveTaxFreeDay((short) 4, (short) 7))).
        when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2013, 6, 4, 6, 0);
    LocalDateTime localDateTime_2 = LocalDateTime.of(2013, 6, 4, 6, 40);
    LocalDateTime localDateTime_3 = LocalDateTime.of(2013, 6, 25, 18, 49);
    LocalDateTime localDateTime_4 = LocalDateTime.of(2013, 7, 4, 15, 0);
    LocalDateTime localDateTime_5 = LocalDateTime.of(2013, 7, 4, 17, 0);
    LocalDateTime localDateTime_6 = LocalDateTime.of(2013, 7, 4, 18, 30);

    var crossingTimes = List.of(localDateTime_1, localDateTime_2, localDateTime_3, localDateTime_4, localDateTime_5,
        localDateTime_6);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(13, congestionTax);
  }

  @Test
  void getCongestionTax_twoIn6JuneOneAtNightRestInJuly_exceptZero() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    doReturn(List.of(getActiveTaxFreeDay((short) 6, (short) 6), getActiveTaxFreeDay((short) 4, (short) 7)))
        .when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2013, 6, 6, 6, 0);
    LocalDateTime localDateTime_2 = LocalDateTime.of(2013, 6, 6, 6, 40);
    LocalDateTime localDateTime_3 = LocalDateTime.of(2013, 6, 25, 18, 49);
    LocalDateTime localDateTime_4 = LocalDateTime.of(2013, 7, 4, 15, 0);
    LocalDateTime localDateTime_5 = LocalDateTime.of(2013, 7, 4, 17, 0);
    LocalDateTime localDateTime_6 = LocalDateTime.of(2013, 7, 4, 18, 30);

    var crossingTimes = List.of(localDateTime_1, localDateTime_2, localDateTime_3, localDateTime_4, localDateTime_5,
        localDateTime_6);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(0.0, congestionTax);
  }

  @Test
  void getCongestionTax_threeWithin60AndOneAt1620RestAtNight_except36() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    doReturn(Collections.emptyList()).when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2023, 11, 14, 15, 0);
    LocalDateTime localDateTime_2 = LocalDateTime.of(2023, 11, 14, 15, 10);
    LocalDateTime localDateTime_3 = LocalDateTime.of(2023, 11, 14, 15, 30);
    LocalDateTime localDateTime_4 = LocalDateTime.of(2023, 11, 14, 16, 20);
    LocalDateTime localDateTime_5 = LocalDateTime.of(2023, 11, 10, 21, 0);
    LocalDateTime localDateTime_6 = LocalDateTime.of(2023, 11, 15, 18, 30);

    var crossingTimes = List.of(localDateTime_1, localDateTime_2, localDateTime_3, localDateTime_4, localDateTime_5,
        localDateTime_6);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(36.0, congestionTax);
  }

  @Test
  void getCongestionTax_threeWithin60AndOneAt1620AnotherAt1829_except36() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    doReturn(Collections.emptyList()).when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2023, 11, 14, 15, 0);
    LocalDateTime localDateTime_2 = LocalDateTime.of(2023, 11, 14, 15, 10);
    LocalDateTime localDateTime_3 = LocalDateTime.of(2023, 11, 14, 15, 30);
    LocalDateTime localDateTime_4 = LocalDateTime.of(2023, 11, 14, 16, 20);
    LocalDateTime localDateTime_5 = LocalDateTime.of(2023, 11, 10, 21, 0);
    LocalDateTime localDateTime_6 = LocalDateTime.of(2023, 11, 15, 18, 29, 59);

    var crossingTimes = List.of(localDateTime_1, localDateTime_2, localDateTime_3, localDateTime_4, localDateTime_5,
        localDateTime_6);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(44.0, congestionTax);
  }

  @Test
  void getCongestionTax_OneAt15OneAt1829RestAtNight_except36() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    doReturn(Collections.emptyList()).when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2023, 11, 14, 15, 0);
    LocalDateTime localDateTime_2 = LocalDateTime.of(2023, 11, 14, 21, 0);
    LocalDateTime localDateTime_3 = LocalDateTime.of(2023, 11, 10, 21, 0);
    LocalDateTime localDateTime_4 = LocalDateTime.of(2023, 11, 15, 18, 29, 59);

    var crossingTimes = List.of(localDateTime_1, localDateTime_2, localDateTime_3, localDateTime_4);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(21.0, congestionTax);
  }

  @Test
  void getCongestionTax_oneInDayBeforeHoliday_exceptZero() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    var holidayDay = getActiveTaxFreeDay((short) 24, (short) 6);
    holidayDay.setHoliday(true);
    doReturn(List.of(holidayDay)).when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2023, 6, 23, 14, 0);

    var crossingTimes = List.of(localDateTime_1);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(0.0, congestionTax);
  }

  @Test
  void getCongestionTax_notBeforeHoliday_except8() throws SimpleApiException {

    doReturn(getCityTaxConfigs()).when(cityTaxConfigPersistenceService).findAllByCity(any());

    doReturn(getTaxExemptedVehicles()).when(taxExemptedVehiclePersistenceService).findAllByCity(anyString());

    doReturn(getTaxRules()).when(taxRulePersistenceService).findAllByCity(anyString());

    var holidayDay = getActiveTaxFreeDay((short) 23, (short) 6);
    holidayDay.setHoliday(true);
    doReturn(List.of(holidayDay)).when(taxFreeDayPersistenceService).findAllByCity(anyString());

    LocalDateTime localDateTime_1 = LocalDateTime.of(2023, 6, 21, 14, 0);

    var crossingTimes = List.of(localDateTime_1);

    double congestionTax = congestionTaxService.getCongestionTax("car", crossingTimes, CITY);

    assertEquals(8.0, congestionTax);
  }

  private static Stream<Arguments> allExemptedVehicles(){

    return Stream.of(Arguments.of("emergency"),
        Arguments.of("bus"),
        Arguments.of("diplomat"),
        Arguments.of("motorcycle"),
        Arguments.of("military"),
        Arguments.of("foreign"));
  }
  private TaxFreeDay getActiveTaxFreeDay(short day, short month) {
    TaxFreeDay taxFreeDay = new TaxFreeDay();
    taxFreeDay.setActive(true);
    taxFreeDay.setCity(getCity());
    taxFreeDay.setDay(day);
    taxFreeDay.setMonth(month);

    return taxFreeDay;
  }

  private List<CityTaxConfig> getCityTaxConfigs() {

    CityTaxConfig cityTaxConfig_1 = new CityTaxConfig();
    cityTaxConfig_1.setConfigKey(CongestionTaxServiceImpl.WEEKEND_TAX_FREE_ENABLED_KEY);
    cityTaxConfig_1.setConfigValue("true");

    CityTaxConfig cityTaxConfig_2 = new CityTaxConfig();
    cityTaxConfig_2.setConfigKey(CongestionTaxServiceImpl.ONE_CHARGE_WITHIN_MINUTES_KEY);
    cityTaxConfig_2.setConfigValue("60");

    CityTaxConfig cityTaxConfig_3 = new CityTaxConfig();
    cityTaxConfig_3.setConfigKey(CongestionTaxServiceImpl.TAX_FREE_DAYS_BEFORE_HOLIDAY_ENABLED);
    cityTaxConfig_3.setConfigValue("true");

    CityTaxConfig cityTaxConfig_4 = new CityTaxConfig();
    cityTaxConfig_4.setConfigKey(CongestionTaxServiceImpl.TAX_FREE_DAYS_BEFORE_HOLIDAY_NUMBER);
    cityTaxConfig_4.setConfigValue("1");

    return List.of(cityTaxConfig_1, cityTaxConfig_2, cityTaxConfig_3, cityTaxConfig_4);
  }

  private List<TaxRule> getTaxRules() {

    TaxRule taxRule_1 = new TaxRule();
    taxRule_1.setCity(getCity());
    taxRule_1.setTaxAmount(8.0);
    taxRule_1.setStartTime(OffsetTime.of(6, 0, 0, 0, ZoneOffset.UTC));
    taxRule_1.setEndTime(OffsetTime.of(6, 29, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_2 = new TaxRule();
    taxRule_2.setCity(getCity());
    taxRule_2.setTaxAmount(13.0);
    taxRule_2.setStartTime(OffsetTime.of(6, 30, 0, 0, ZoneOffset.UTC));
    taxRule_2.setEndTime(OffsetTime.of(6, 59, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_3 = new TaxRule();
    taxRule_3.setCity(getCity());
    taxRule_3.setTaxAmount(18.0);
    taxRule_3.setStartTime(OffsetTime.of(7, 0, 0, 0, ZoneOffset.UTC));
    taxRule_3.setEndTime(OffsetTime.of(7, 59, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_4 = new TaxRule();
    taxRule_4.setCity(getCity());
    taxRule_4.setTaxAmount(13.0);
    taxRule_4.setStartTime(OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC));
    taxRule_4.setEndTime(OffsetTime.of(8, 29, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_5 = new TaxRule();
    taxRule_5.setCity(getCity());
    taxRule_5.setTaxAmount(8.0);
    taxRule_5.setStartTime(OffsetTime.of(8, 30, 0, 0, ZoneOffset.UTC));
    taxRule_5.setEndTime(OffsetTime.of(14, 59, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_6 = new TaxRule();
    taxRule_6.setCity(getCity());
    taxRule_6.setTaxAmount(13.0);
    taxRule_6.setStartTime(OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC));
    taxRule_6.setEndTime(OffsetTime.of(15, 29, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_7 = new TaxRule();
    taxRule_7.setCity(getCity());
    taxRule_7.setTaxAmount(18.0);
    taxRule_7.setStartTime(OffsetTime.of(15, 30, 0, 0, ZoneOffset.UTC));
    taxRule_7.setEndTime(OffsetTime.of(16, 59, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_8 = new TaxRule();
    taxRule_8.setCity(getCity());
    taxRule_8.setTaxAmount(13.0);
    taxRule_8.setStartTime(OffsetTime.of(17, 0, 0, 0, ZoneOffset.UTC));
    taxRule_8.setEndTime(OffsetTime.of(17, 59, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_9 = new TaxRule();
    taxRule_9.setCity(getCity());
    taxRule_9.setTaxAmount(8.0);
    taxRule_9.setStartTime(OffsetTime.of(18, 0, 0, 0, ZoneOffset.UTC));
    taxRule_9.setEndTime(OffsetTime.of(18, 29, 0, 0, ZoneOffset.UTC));

    TaxRule taxRule_10 = new TaxRule();
    taxRule_10.setCity(getCity());
    taxRule_10.setTaxAmount(0.0);
    taxRule_10.setStartTime(OffsetTime.of(18, 30, 0, 0, ZoneOffset.UTC));
    taxRule_10.setEndTime(OffsetTime.of(5, 59, 0, 0, ZoneOffset.UTC));

    return List.of(taxRule_1, taxRule_2, taxRule_3, taxRule_4, taxRule_5, taxRule_6, taxRule_7, taxRule_8, taxRule_9,
        taxRule_10);
  }

  private City getCity() {

    City city = new City();
    city.setName(CITY);

    return city;
  }

  private List<TaxExemptedVehicle> getTaxExemptedVehicles() {

    Vehicle vehicle_1 = new Vehicle();
    vehicle_1.setType("Emergency");
    TaxExemptedVehicle taxExemptedVehicle_1 = new TaxExemptedVehicle();
    taxExemptedVehicle_1.setVehicle(vehicle_1);
    taxExemptedVehicle_1.setCity(getCity());
    taxExemptedVehicle_1.setActive(true);

    Vehicle vehicle_2 = new Vehicle();
    vehicle_2.setType("Bus");
    TaxExemptedVehicle taxExemptedVehicle_2 = new TaxExemptedVehicle();
    taxExemptedVehicle_2.setVehicle(vehicle_2);
    taxExemptedVehicle_2.setCity(getCity());
    taxExemptedVehicle_2.setActive(true);

    Vehicle vehicle_3 = new Vehicle();
    vehicle_3.setType("Diplomat");
    TaxExemptedVehicle taxExemptedVehicle_3 = new TaxExemptedVehicle();
    taxExemptedVehicle_3.setVehicle(vehicle_3);
    taxExemptedVehicle_3.setCity(getCity());
    taxExemptedVehicle_3.setActive(true);

    Vehicle vehicle_4 = new Vehicle();
    vehicle_4.setType("MotorCycle");
    TaxExemptedVehicle taxExemptedVehicle_4 = new TaxExemptedVehicle();
    taxExemptedVehicle_4.setVehicle(vehicle_4);
    taxExemptedVehicle_4.setCity(getCity());
    taxExemptedVehicle_4.setActive(true);

    Vehicle vehicle_5 = new Vehicle();
    vehicle_5.setType("Military");
    TaxExemptedVehicle taxExemptedVehicle_5 = new TaxExemptedVehicle();
    taxExemptedVehicle_5.setVehicle(vehicle_5);
    taxExemptedVehicle_5.setCity(getCity());
    taxExemptedVehicle_5.setActive(true);

    Vehicle vehicle_6 = new Vehicle();
    vehicle_6.setType("Foreign");
    TaxExemptedVehicle taxExemptedVehicle_6 = new TaxExemptedVehicle();
    taxExemptedVehicle_6.setVehicle(vehicle_6);
    taxExemptedVehicle_6.setCity(getCity());
    taxExemptedVehicle_6.setActive(true);

    return List.of(taxExemptedVehicle_1, taxExemptedVehicle_2, taxExemptedVehicle_3, taxExemptedVehicle_4,
        taxExemptedVehicle_5, taxExemptedVehicle_6);
  }

}
