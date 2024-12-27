package com.gl.ceir.panel.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.response.GroupFeaturePaginationDto;
import com.gl.ceir.panel.dto.response.GroupFeatureViewDto;
import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupFeatureEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupFeatureMapper {
	GroupFeaturePaginationDto groupEntityToGroupFeatureDto(GroupEntity ue);
	List<GroupFeatureViewDto> entityToDto(List<GroupFeatureEntity> ge);
}
