package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.gl.ceir.panel.entity.app.RoleFeatureModuleAccessEntity;
import com.gl.ceir.panel.entity.app.UserEntity;

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
public class UserGroupPermissionDto implements Serializable{
	private static final long serialVersionUID = -960158134061411564L;
	private UserEntity userEntity;
	private List<RoleFeatureModuleAccessEntity> acls;
	private Set<Long> userIds;
	private List<PermissionDto> permissions;
}
