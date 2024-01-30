package com.volvo.congestiontax.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cities")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cities_id_sequence")
  @SequenceGenerator(name = "cities_id_sequence", sequenceName = "cities_id_seq", allocationSize = 1)
  private Long Id;

  @Column(name = "sid", unique = true, updatable = false, nullable = false)
  private String sid;

  @Column(name = "name", unique = true, nullable = false)
  private String name;
}
