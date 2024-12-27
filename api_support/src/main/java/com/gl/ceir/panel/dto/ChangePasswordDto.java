package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto implements Serializable{
	private static final long serialVersionUID = 5576583569982571203L;
	private String oldPassword;
	private String newPassword;
	private String confirmNewPassword;
}
