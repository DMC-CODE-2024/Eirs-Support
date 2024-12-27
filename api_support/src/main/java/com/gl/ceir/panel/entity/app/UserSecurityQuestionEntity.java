package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name="user_security_question")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserSecurityQuestionEntity extends AbstractTimestampEntity implements Serializable{
	private static final long serialVersionUID = 3142802852506994342L;
	@EmbeddedId
	private UserSecurityQuestionId id;
	@Column(length = 255)
	private String answer;
}
