package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CheckCountryDto implements Serializable{
	private static final long serialVersionUID = 3036292616916071914L;
	private String ip;
	private String ipType;
}
