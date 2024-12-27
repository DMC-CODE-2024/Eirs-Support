package com.gl.ceir.panel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.service.LanguageService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/language")
@CrossOrigin
public class LanguageController {
	private final LanguageService languageService;
	@GetMapping("/{language}")
	public ResponseEntity<?> getById(@PathVariable String language) {
		log.info("language: {}", language);
		return new ResponseEntity<>(languageService.languagejson(language), HttpStatus.OK);
	}
}
