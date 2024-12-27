package com.gl.ceir.panel.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.response.GroupRoleViewDto;
import com.gl.ceir.panel.entity.app.GroupRoleEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupRoleMapper {
	List<GroupRoleViewDto> entityToDto(List<GroupRoleEntity> ge);
}
