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
public class TicketResponseDto implements Serializable{
	private static final long serialVersionUID = -3831821639391642227L;
	@JsonAlias("ticket_id")
	private String ticketId;
	@JsonAlias("first_name")
	private String firstName;
	@JsonAlias("last_name")
	private String lastName;
	@JsonAlias("mobile_number")
	private String mobileNumber;
	@JsonAlias("email_address")
	private String emailAddress;
	private String category;
	@JsonAlias("user_id")
	private String userId;
	@JsonAlias("user_type")
	private String userType;
	@JsonAlias("raised_by")
	private String raisedBy;
	private TicketIssueDto issue;
	@JsonAlias("isPrivate")
	private boolean isPrivate;
	private String message;
}
