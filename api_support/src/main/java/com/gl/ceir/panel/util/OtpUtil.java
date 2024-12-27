package com.gl.ceir.panel.util;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OtpUtil {
	@Value("${eirs.is.testing.enabled:true}")
	private boolean isTestingEnabled;
	@Value("${eirs.allowed.otp.length:6}")
	private int otpLength;
	private final static String SALTCHARS = "1234567890";
	public String phoneOtp(String mobile) {
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < otpLength) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String otpval = salt.toString();
		return isTestingEnabled ? "123456" : otpval;
	}
}
