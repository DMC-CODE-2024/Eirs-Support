package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PaginationRequestDto implements Serializable{
	private static final long serialVersionUID = 2403464455920087905L;
	private Integer pageNo;
	private Integer pageSize;
	private PageDto page;
	private List<FilterDto> filters;
	private SortDto sort;
}
