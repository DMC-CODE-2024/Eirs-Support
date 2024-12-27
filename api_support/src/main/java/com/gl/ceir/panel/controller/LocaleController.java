package com.gl.ceir.panel.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.DeviceRequest;
import com.gl.ceir.panel.dto.LocaleDto;
import com.gl.ceir.panel.service.LocaleService;

import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = "http://localhost:4200")
@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("locale")
public class LocaleController {
	@Autowired
	private LocaleService localeService; 
	
	@GetMapping("list")
	public @ResponseBody List<LocaleDto> list() throws IOException {
		return localeService.getLangCodes();
	}
}
