package com.gl.ceir.panel.dto.response;

import java.io.Serializable;

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
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = 3851888651538943509L;
	private String apiResult;
	private String message;
	private String token;
	@Default
	private String type = "Bearer";
	private Long id;
	private String userName;
	private String email;
	private boolean isTemparoryPassword;
	@Default
	private long remainingExpiryDays = 0l;
}
