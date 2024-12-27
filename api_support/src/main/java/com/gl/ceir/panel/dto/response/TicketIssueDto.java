package com.gl.ceir.panel.dto.response;

import java.io.Serializable;
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
public class TicketIssueDto implements Serializable{
	private static final long serialVersionUID = 222924044547178142L;
	private String id;
	private String subject;
	private String description;
	@JsonAlias("start_date")
	private String startDate;
	@JsonAlias("created_on")
	private String createdOn;
	@JsonAlias("updated_on")
	private String updatedOn;
	private List<TickerJournalsDto> journals;
	private TicketStatusDto status;
	@JsonAlias("attachments")
	private List<RedminUploadDto> uploads;
}
