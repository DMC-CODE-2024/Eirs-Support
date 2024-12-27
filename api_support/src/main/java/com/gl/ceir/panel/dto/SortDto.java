package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class SortDto implements Serializable {
	private static final long serialVersionUID = -5924747307856200262L;
	private String by;
	private boolean reverse;
}
