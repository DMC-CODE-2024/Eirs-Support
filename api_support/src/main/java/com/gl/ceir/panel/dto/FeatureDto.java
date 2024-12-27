package com.gl.ceir.panel.dto;

import java.io.Serializable;



import org.springframework.web.multipart.MultipartFile;

import com.gl.ceir.panel.entity.app.AbstractTimestampEntity;

import jakarta.validation.constraints.NotBlank;
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
public class FeatureDto extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 7473275252882625029L;
	private Long id;
	@NotBlank(message = "Name is mandatory")
	private String featureName;
	private String description;
	private Long defaultModuleId;
	private String category;
	private Long moduleTagId;
	private String link;
	private MultipartFile file;
	private String logo;
}
