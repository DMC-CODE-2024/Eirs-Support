package com.gl.ceir.panel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.service.SecurityQuestionService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/security")
public class SecurityQuestionController {
	private final SecurityQuestionService securityQuestionService;

	@GetMapping("questions")
	public ResponseEntity<?> list() {
		return new ResponseEntity<>(securityQuestionService.list(), HttpStatus.OK);
	}
}
