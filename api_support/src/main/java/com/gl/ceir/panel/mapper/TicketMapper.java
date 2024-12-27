package com.gl.ceir.panel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.gl.ceir.panel.dto.TicketDto;
import com.gl.ceir.panel.entity.app.TicketEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
	TicketEntity ticketDtoToEntity(TicketDto ticketDto);
	TicketDto ticketEntityToDto(TicketEntity ticketEntity);  
}
