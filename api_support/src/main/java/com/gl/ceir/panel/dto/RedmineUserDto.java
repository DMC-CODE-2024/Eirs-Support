package com.gl.ceir.panel.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class RedmineUserDto implements Serializable{
	private static final long serialVersionUID = -2736084824205579541L;
	private Long id;
	private String login;
	private String password;
	private String firstname;
	private String lastname;
	private String mail;
	@Default
	private int authSourceId = 2;
	@Default
	private boolean admin =true;
}
