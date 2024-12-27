package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="operator_village")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class VillageEntity implements Serializable{
	private static final long serialVersionUID = -6525112908440677714L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length =  32)
	private String name;
	@ManyToOne
    @JoinColumn(name="commune_id", nullable=false, referencedColumnName = "id")
	@JsonBackReference
    private CommuneEntity commune;
}
