package com.gl.ceir.panel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.repository.app.CountryRepository;
import com.gl.ceir.panel.repository.app.ProvinceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("country")
@RequiredArgsConstructor
public class CountryController {
	private final CountryRepository countryRepository;
	private final ProvinceRepository provinceRepository;

	@GetMapping("list")
	public @ResponseBody ResponseEntity<?> accessList() {
		return new ResponseEntity<>(countryRepository.findByName("Cambodia"), HttpStatus.OK);
	}
	
	@GetMapping("provinces")
	public @ResponseBody ResponseEntity<?> province() {
		return new ResponseEntity<>(provinceRepository.findAll(), HttpStatus.OK);
	}
}
