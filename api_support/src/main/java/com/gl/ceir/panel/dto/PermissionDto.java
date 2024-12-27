package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionDto implements Serializable{
	private static final long serialVersionUID = -3247112772967926114L;
	private String tag;
	@Default
	private Set<String> modules = new HashSet<String>();
}
