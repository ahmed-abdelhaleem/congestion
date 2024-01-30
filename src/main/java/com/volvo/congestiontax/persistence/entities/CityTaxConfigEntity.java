package com.volvo.congestiontax.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "city_tax_configs", uniqueConstraints = {@UniqueConstraint(columnNames = {"config_key", "city_id"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityTaxConfigEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_tax_configs_id_sequence")
  @SequenceGenerator(name = "city_tax_configs_id_sequence", sequenceName = "city_tax_configs_id_seq", allocationSize = 1)
  private Long Id;

  @Column(name = "sid", unique = true, updatable = false, nullable = false)
  private String sid;

  @Column(name = "config_key", nullable = false)
  private String configKey;

  @Column(name = "config_value", nullable = false)
  private String configValue;

  @ManyToOne(targetEntity = CityEntity.class)
  @JoinColumn(name = "city_id", nullable = false)
  private CityEntity city;

}
