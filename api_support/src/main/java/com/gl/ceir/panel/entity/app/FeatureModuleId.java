package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeatureModuleId implements Serializable {
	private static final long serialVersionUID = -6935111139848074302L;
	@Column(name = "feature_id")
	private Long featureId;
	@Column(name = "module_id")
	private Long moduleId;
}
