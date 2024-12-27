package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gl.ceir.panel.dto.response.RedminUploadDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketDto  implements Serializable {
	private static final long serialVersionUID = 7473275252882625029L;
	@JsonAlias("ticket_id")
	private String ticketId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String emailAddress;
	private String category;
	private String subject;
	private String description;
	private String userId;
	@Default
	private String userType = "END_USER";
	@Default
	private String raisedBy = "Public"; //Public/User/CustomerCare
	@Default
	private String notes = "Testing";
	private boolean privateNotes;
	@JsonAlias("isPrivate")
	@JsonProperty("isPrivate")
	private boolean isPrivate;
	
	private String alternateMobileNumber;
	private Long categoryId;
	private String currentStatus;
	private MultipartFile[] documents;
	@Default
	private String fileWithDocuments = "{}";
	private List<RedminUploadDto> attachments;
	
	@JsonAlias("created_on")
	private String createdOn;
	@JsonAlias("updated_on")
	private String updatedOn;
	private boolean resolved;
	@Default
	private String referenceId = "";
	@Default
	private String transactionId = "";
	
	private String address;
	private String province;
	private String district;
	private String commune;
}
