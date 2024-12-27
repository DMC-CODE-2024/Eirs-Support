package com.gl.ceir.panel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.TicketCategoryDto;
import com.gl.ceir.panel.entity.app.TicketCategoryEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketCategoryMapper {
	TicketCategoryEntity ticketDtoToEntity(TicketCategoryDto ticketDto);
	TicketCategoryDto ticketEntityToDto(TicketCategoryEntity ticketEntity);  
}
