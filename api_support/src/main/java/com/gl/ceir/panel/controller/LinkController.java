package com.gl.ceir.panel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.constant.UrlTypeEnum;
import com.gl.ceir.panel.entity.app.LinkEntity;
import com.gl.ceir.panel.service.LinkService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/link")
public class LinkController {
	private final LinkService linkService;

	@PostMapping("/save")
	public ResponseEntity<?> registerUser(@Valid @RequestBody LinkEntity linkEntity) {
		return ResponseEntity.ok(linkService.save(linkEntity));
	}

	@GetMapping("list")
	public ResponseEntity<?> list() {
		return new ResponseEntity<>(linkService.links(), HttpStatus.OK);
	}
	@GetMapping("list/{urlTypeEnum}")
	public ResponseEntity<?> list(@PathVariable UrlTypeEnum urlTypeEnum) {
		return new ResponseEntity<>(linkService.linksByUrlType(urlTypeEnum), HttpStatus.OK);
	}
	@GetMapping("findByUrl/{url}")
	public ResponseEntity<?> list(@PathVariable String url) {
		return new ResponseEntity<>(linkService.findByUrl(url), HttpStatus.OK);
	}
}
