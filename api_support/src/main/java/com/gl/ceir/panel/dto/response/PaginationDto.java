package com.gl.ceir.panel.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto implements Serializable{
	private static final long serialVersionUID = -9020774972517016107L;
	private int totalElements;
	private int numberOfElements;
	private int totalPages;
	private int currentPage;
	@JsonAlias("data")
	@Default
	private List<TicketResponseDto> content = new ArrayList<TicketResponseDto>();
}
