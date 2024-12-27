package com.gl.ceir.panel.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class JwtData implements Serializable{
	private static final long serialVersionUID = -4748336961666435273L;
	private String sub;
	private long iat;
	private long exp;
}
