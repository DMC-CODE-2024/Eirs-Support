package com.gl.ceir.downloader.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "attachments")
@JsonInclude(Include.NON_NULL)
public class AttachmentEntity {
	@Id
	private Long id;
	private Long containerId;
	private String containerType;
	private String filename;
	private String diskFilename;
	private long filesize;
	private String contentType;
	private String digest;
	private int downloads;
	private int authorId;
	private LocalDateTime createdOn;
	private String description;
	private String diskDirectory;
}
