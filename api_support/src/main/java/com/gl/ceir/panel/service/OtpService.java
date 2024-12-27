package com.gl.ceir.panel.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.controller.OtpController;
import com.gl.ceir.panel.util.OtpUtil;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@SuppressWarnings("unused")
@Log4j2
@AllArgsConstructor
@Service
public class OtpService {
	private final OtpUtil otpUtil;
}
