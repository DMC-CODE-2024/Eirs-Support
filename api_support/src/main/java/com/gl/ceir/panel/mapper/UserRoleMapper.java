package com.gl.ceir.panel.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.response.UserRoleViewDto;
import com.gl.ceir.panel.entity.app.UserRoleEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRoleMapper {
	List<UserRoleViewDto> entityToDto(List<UserRoleEntity> ge);
}
