package com.gl.ceir.panel.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
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
public class LoginRequest implements Serializable {
	private static final long serialVersionUID = 1062630280730462478L;
	@NotBlank
	private String userName;
	@NotBlank
	private String password;
}
