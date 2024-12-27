package com.gl.ceir.panel.dto.response;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TickerJournalsDto implements Serializable{
	private static final long serialVersionUID = -5084962247285757461L;
	private String id;
	private String notes;
	@JsonAlias("created_on")
	private ZonedDateTime createdOn;
	@JsonAlias("private_notes")
	private boolean privateNotes;
	private TickerUserDto user;
	private List<AttachmentDetailsDto> details;
}
