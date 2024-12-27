package com.gl.ceir.panel.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.response.FeatureModuleViewDto;
import com.gl.ceir.panel.entity.app.FeatureModuleEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeatureModuleMapper {
	List<FeatureModuleViewDto> entityToDto(List<FeatureModuleEntity> ge);
}
