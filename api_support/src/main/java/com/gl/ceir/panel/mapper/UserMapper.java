package com.gl.ceir.panel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.dto.UserUpdateDto;
import com.gl.ceir.panel.dto.request.SignupRequest;
import com.gl.ceir.panel.entity.app.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	UserEntity userDtoToEntity(UserCreateDto ucd);
	UserCreateDto userEntityToDto(UserEntity ue);
	UserCreateDto signupDtoToUserCreateDto(SignupRequest signUpRequest);
	UserCreateDto toCreateDto(UserUpdateDto upd);  
}
