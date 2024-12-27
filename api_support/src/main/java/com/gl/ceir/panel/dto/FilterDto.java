package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FilterDto implements Serializable {
	private static final long serialVersionUID = -1355223365066712438L;
	private String property;
	private String value;
}
