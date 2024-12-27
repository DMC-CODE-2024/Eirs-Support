package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.List;

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
public class UserGroupDto implements Serializable {
	private static final long serialVersionUID = 7473275252882625029L;
	private Long id;
	private Long userId;
	private List<Long> groups;
	private List<GroupOrderDto> groupmap;
}
