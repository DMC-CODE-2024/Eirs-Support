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
public class AttachmentDetailsDto implements Serializable{
	private static final long serialVersionUID = -8652329831742766530L;
	private String property;
	private String name;
	@JsonAlias("new_value")
	private String newValue;
	
	private String token;
	private String filename;
	private String contentType;
	private int filesize;
	private String contentUrl;
	private String description;
}
