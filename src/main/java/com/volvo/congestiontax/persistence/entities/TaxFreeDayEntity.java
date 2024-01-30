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
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "tax_free_days", uniqueConstraints = {@UniqueConstraint(columnNames = {"tax_day", "tax_month"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxFreeDayEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_free_days_id_sequence")
  @SequenceGenerator(name = "tax_free_days_id_sequence", sequenceName = "tax_free_days_id_seq", allocationSize = 1)
  private Long Id;

  @Column(name = "sid", unique = true, updatable = false, nullable = false)
  private String sid;

  @Column(name = "tax_day", nullable = false)
  @Range(min = 1, max = 31)
  private short day;

  @Range(min = 1, max = 12)
  @Column(name = "tax_month", nullable = false)
  private short month;

  @Column(name = "active", nullable = false)
  private boolean active;

  @Column(name = "holiday", nullable = false)
  private boolean holiday;

  @ManyToOne(targetEntity = CityEntity.class)
  @JoinColumn(name="city_id", nullable=false)
  private CityEntity city;

}
