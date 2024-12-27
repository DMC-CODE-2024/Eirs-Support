package com.gl.ceir.panel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.entity.app.UserProfileEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
	UserProfileEntity userProfileDtoToEntity(UserCreateDto ucd);

	UserCreateDto userProfileEntityToDto(UserProfileEntity ue);
}
