package com.gl.ceir.panel.dto.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatusDto implements Serializable{
	private static final long serialVersionUID = -1962727986472547630L;
	private String id;
	private String name;
	@JsonAlias("is_closed")
	private boolean isClosed;
}
