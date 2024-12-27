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

import com.gl.ceir.panel.dto.FeatureModuleDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.entity.app.FeatureModuleId;
import com.gl.ceir.panel.service.FeatureModuleService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("featureModule")
@RequiredArgsConstructor
public class FeatureModuleController {
	private final FeatureModuleService featureModuleService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody FeatureModuleDto featureModuleDto, HttpServletRequest request) {
		return new ResponseEntity<>(featureModuleService.save(featureModuleDto,request), HttpStatus.OK);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody FeatureModuleDto featureModuleDto, @PathVariable Long id, HttpServletRequest request) {
		log.info("Role:{},update request:{}", id, featureModuleDto);
		return new ResponseEntity<>(featureModuleService.update(featureModuleDto, id,request), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(featureModuleService.pagination(ulrd), HttpStatus.OK);
	}
	@GetMapping("list")
	public ResponseEntity<?> findRoles() {
		return new ResponseEntity<>(featureModuleService.getFeatureModules(), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<FeatureModuleId> ids) {
		return new ResponseEntity<>(featureModuleService.delete(ids), HttpStatus.OK);
	} 
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<FeatureModuleId> ids) {
		return new ResponseEntity<>(featureModuleService.active(ids), HttpStatus.OK);
	}
	@GetMapping("/{featureId}")
	public ResponseEntity<?> getById(@PathVariable Long featureId){
		return new ResponseEntity<>(featureModuleService.getById(featureId), HttpStatus.OK);
	}
}
