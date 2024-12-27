package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "operator_series")
@JsonInclude(Include.NON_NULL)
public class OperatorSeriesEntity extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 4195523412451054160L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int seriesStart;
	private int seriesEnd;
	private String seriesType;
	private String operatorName;
	private int userId;
	private String length;
	private String remark;
}