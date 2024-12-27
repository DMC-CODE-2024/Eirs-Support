package com.gl.ceir.panel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserFeatureDto;
import com.gl.ceir.panel.entity.app.UserFeatureId;
import com.gl.ceir.panel.service.UserFeatureService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("userFeature")
@RequiredArgsConstructor
public class UserFeatureController {
	private final UserFeatureService userFeatureService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody UserFeatureDto userFeatureDto, HttpServletRequest request) {
		return new ResponseEntity<>(userFeatureService.save(userFeatureDto,request), HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id){
		return new ResponseEntity<>(userFeatureService.getById(id), HttpStatus.OK);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody UserFeatureDto userFeatureDto, @PathVariable Long id, HttpServletRequest request) {
		log.info("Role:{},update request:{}", id, userFeatureDto);
		return new ResponseEntity<>(userFeatureService.update(userFeatureDto, id,request), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<UserFeatureId> ids, HttpServletRequest request) {
		return new ResponseEntity<>(userFeatureService.delete(ids,request), HttpStatus.OK);
	} 
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<UserFeatureId> ids) {
		return new ResponseEntity<>(userFeatureService.active(ids), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(userFeatureService.pagination(ulrd), HttpStatus.OK);
	}
}
