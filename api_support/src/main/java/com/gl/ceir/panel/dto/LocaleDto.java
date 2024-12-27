package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocaleDto implements Serializable {
	private static final long serialVersionUID = 7829796024611766728L;
	private String code;
	private String label;
}
