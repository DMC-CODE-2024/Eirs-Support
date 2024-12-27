package com.gl.ceir.panel.dto.response;

import java.io.Serializable;
import java.util.List;

import com.gl.ceir.panel.dto.RoleDto;
import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.entity.app.UserRoleId;

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
public class UserRoleViewDto implements Serializable {
	private static final long serialVersionUID = -7574572958525730695L;
	private String userName;
	private List<RoleDto> roles;
	
	private UserRoleId id;
	private UserCreateDto user;
	private RoleDto role;
}
