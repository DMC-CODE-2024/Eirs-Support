package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeatureOrderDto implements Serializable{
	private static final long serialVersionUID = -4874176333654737465L;
	private Long featureId;
	private int displayOrder;
}
