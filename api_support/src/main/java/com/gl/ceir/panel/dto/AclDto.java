package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.List;

import com.gl.ceir.panel.entity.app.AbstractTimestampEntity;

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
public class AclDto extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 7473275252882625029L;
	private Long id;
	private Long roleId;
	private List<Long> featureModules;
	private String featureModuleOverrideLink;
	private List<AclFeatureDto> features;
}
