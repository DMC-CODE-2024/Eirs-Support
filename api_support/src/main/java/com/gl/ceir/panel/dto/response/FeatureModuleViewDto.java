package com.gl.ceir.panel.dto.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gl.ceir.panel.dto.FeatureDto;
import com.gl.ceir.panel.dto.ModuleDto;
import com.gl.ceir.panel.entity.app.FeatureModuleId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class FeatureModuleViewDto implements Serializable {
	private static final long serialVersionUID = -7574572958525730695L;
	private String userName;
	private List<ModuleDto> features;
	private FeatureModuleId id;
	private FeatureDto feature;
	private ModuleDto module;
}
