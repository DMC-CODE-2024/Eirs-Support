package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PageDto implements Serializable {
	private static final long serialVersionUID = 880781294441794921L;
	private Integer from;
	private Integer to;
	private Integer size;
	private Integer current;
}
