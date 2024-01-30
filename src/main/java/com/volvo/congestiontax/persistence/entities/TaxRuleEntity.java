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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import java.time.OffsetTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tax_rules", uniqueConstraints = {@UniqueConstraint(columnNames = {"start_time", "end_time"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRuleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_rules_id_sequence")
  @SequenceGenerator(name = "tax_rules_id_sequence", sequenceName = "tax_rules_id_seq", allocationSize = 1)
  private Long Id;

  @Column(name = "sid", unique = true, updatable = false, nullable = false)
  private String sid;

  @Column(name = "start_time")
  @Temporal(TemporalType.TIME)
  private OffsetTime startTime;

  @Column(name = "end_time")
  @Temporal(TemporalType.TIME)
  private OffsetTime endTime;

  @Column(name = "tax_amount")
  private Double taxAmount;

  @ManyToOne(targetEntity=CityEntity.class)
  @JoinColumn(name="city_id", nullable=false)
  private CityEntity city;
}
