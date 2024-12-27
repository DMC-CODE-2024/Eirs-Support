package com.gl.ceir.panel.entity.app;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="province_db")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceEntity extends AbstractTimestampEntity implements Serializable{
	private static final long serialVersionUID = -6525112908440677714L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length =  50, name="province")
	private String name;
	@Column(length =  50, name="province_km")
	private String provinceKm;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "province")
	@JsonManagedReference
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<DistrictEntity> districts;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="country", nullable=false, referencedColumnName = "country_name")
	@JsonBackReference
    private CountryEntity country;
}
