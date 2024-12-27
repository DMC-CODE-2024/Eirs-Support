package com.gl.ceir.panel.dto.response;

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
public class AuthDto {
	private boolean isLogin;
	private boolean isPasswordExpire;
	private boolean isTemparoryPassword;
	private Long id;
	private String userName;
	private String email;
}
