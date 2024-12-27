package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketUploadDto implements Serializable{
	private static final long serialVersionUID = -4200065318716342731L;
	private String id;
	private String token;
}
