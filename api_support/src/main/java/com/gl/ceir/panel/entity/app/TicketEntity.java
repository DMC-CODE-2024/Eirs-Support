package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import com.gl.ceir.panel.constant.RaisedByEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "ticket_entity")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TicketEntity extends AbstractTimestampEntity implements Serializable{
	private static final long serialVersionUID = -323966423645371509L;
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String alternateMobileNumber;
	private Long categoryId;
	private String emailAddress;
	@Column(length = 128)
	private String subject;
	@Column(length = 512)
	private String description;
	private Long createdBy;
	private Long modifiedBy;
	@Column(length=2,columnDefinition = "varchar(2) default '0'")
	private String currentStatus;
	private boolean isPrivate;
	@Enumerated(EnumType.STRING)
	private RaisedByEnum raisedBy;
}
