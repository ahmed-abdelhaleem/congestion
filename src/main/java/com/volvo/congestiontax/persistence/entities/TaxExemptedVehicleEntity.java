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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tax_exempted_vehicles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxExemptedVehicleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_exempted_vehicles_id_sequence")
  @SequenceGenerator(name = "tax_exempted_vehicles_id_sequence", sequenceName = "tax_exempted_vehicles_id_seq", allocationSize = 1)
  private Long Id;

  @Column(name = "sid", unique = true, updatable = false, nullable = false)
  private String sid;

  @Column(name = "active", nullable = false)
  private boolean active;

  @ManyToOne(targetEntity = VehicleEntity.class)
  @JoinColumn(name="vehicle_id", nullable=false)
  private VehicleEntity vehicle;

  @ManyToOne(targetEntity = CityEntity.class)
  @JoinColumn(name="city_id", nullable=false)
  private CityEntity city;

}
