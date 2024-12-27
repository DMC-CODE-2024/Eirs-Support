package com.gl.ceir.panel.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TickerUserDto implements Serializable{
	private static final long serialVersionUID = -5084962247285757461L;
	private String id;
	private String name;
}
