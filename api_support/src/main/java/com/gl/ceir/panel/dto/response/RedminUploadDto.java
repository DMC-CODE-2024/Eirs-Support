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
public class RedminUploadDto implements Serializable{
	private static final long serialVersionUID = -5224797131647891695L;
	private Long id;
	private String token;
	private String filename;
	@JsonAlias("content_type")
	private String contentType;
	private int filesize;
	@JsonAlias("content_url")
	private String contentUrl;
	private String description;
}
