package com.gl.ceir.panel.dto.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtpDto implements Serializable{
	private static final long serialVersionUID = -4855928708924930766L;
	private String mobileNumber;
	private String otp;
}
