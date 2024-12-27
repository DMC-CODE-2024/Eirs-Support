package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AclTreeDto implements Serializable {
	private static final long serialVersionUID = -5663217590119140633L;
	private Long id;
	private String name;
	private boolean selected;
	private boolean disabled;
	private boolean expanded;
	@Default
	private List<AclTreeDto> childs = new ArrayList<>();
}
