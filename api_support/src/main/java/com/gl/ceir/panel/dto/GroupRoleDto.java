package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gl.ceir.panel.entity.app.AbstractTimestampEntity;

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
public class GroupRoleDto extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 7473275252882625029L;
	private Long id;
	private Long groupId;
	@Default
	private List<Long> roles = new ArrayList<>();
}
