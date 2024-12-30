package com.gl.ceir.panel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.PaginationRequestDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("config")
@AllArgsConstructor
@NoArgsConstructor
public class ConfigController {
	@Value("${eirs.site.key:}")
	private String siteKey;
	@Value("${eirs.portal.link:}")
	private String eirsPortalLink;
	@Value("${eirs.email.regex:}")
	private String emailRegex;
	@Value("${eirs.mobile.regex:}")
	private String mobileRegex;
	@Value("${eirs.mobile.regex.with.blank:}")
	private String mobileRegexWithBlank;
	@Value("${mdr.portal.url:}")
	private String mdrPortalUrl;
	@Value("${sys_param.file.max-file-record:1000}")
	private long rowSizeForExport;
	
	@PostMapping("system/pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	@GetMapping("frontend")
	public ResponseEntity<?> frontend() {
		return new ResponseEntity<>(readFrontendConfig(), HttpStatus.OK);
	}
	
	private Map<String, Object> readFrontendConfig(){
		Map<String, Object> map = new HashMap<>();
		map.put("siteKey", siteKey);
		map.put("eirsPortalLink", eirsPortalLink);
		map.put("emailRegex", emailRegex);
		map.put("mobileRegex", mobileRegex);
		map.put("mobileRegexWithBlank", mobileRegexWithBlank);
		map.put("mdrPortalUrl", mdrPortalUrl);
		map.put("rowSizeForExport", rowSizeForExport);
		return map;
	}
}
