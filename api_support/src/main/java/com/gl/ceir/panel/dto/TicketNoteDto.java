package com.gl.ceir.panel.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gl.ceir.panel.dto.response.RedminUploadDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TicketNoteDto implements Serializable{
	private static final long serialVersionUID = 3036292616916071914L;
	private String ticketId;
	private String notes;
	private boolean privateNotes;
	private MultipartFile[] documents;
	private List<RedminUploadDto> attachments;
	@Default
	private String fileWithDocuments = "{}";
}
