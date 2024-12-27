package com.gl.ceir.panel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Greeting {
	private String content;
}
