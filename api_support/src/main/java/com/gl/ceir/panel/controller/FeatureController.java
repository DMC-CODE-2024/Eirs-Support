package com.gl.ceir.panel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.FeatureDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.service.FeatureService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("feature")
@AllArgsConstructor
public class FeatureController {
	private final FeatureService featureService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@ModelAttribute FeatureDto featureDto, HttpServletRequest request) {
		log.info("feature: {}", featureDto);
		return new ResponseEntity<>(featureService.save(featureDto,request), HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id){
		return new ResponseEntity<>(featureService.getById(id), HttpStatus.OK);
	}
	@RequestMapping(path = "/update/{id}", method = RequestMethod.PUT, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> update(@Valid @ModelAttribute FeatureDto featureDto, @PathVariable Long id, HttpServletRequest request) {
		log.info("Feature:{}", featureDto);
		return new ResponseEntity<>(featureService.update(featureDto, id,request), HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		return new ResponseEntity<>(featureService.deleteById(id), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(featureService.pagination(ulrd), HttpStatus.OK);
	}
	@GetMapping("list/{roleId}")
	public ResponseEntity<?> findFeaturesByRoleId(@PathVariable Long roleId) {
		return new ResponseEntity<>(featureService.getAllFeaturesByRoleId(roleId), HttpStatus.OK);
	}
	@GetMapping("list")
	public ResponseEntity<?> findFeatures() {
		return new ResponseEntity<>(featureService.getFeatures(), HttpStatus.OK);
	}
	@GetMapping("all/list")
	public ResponseEntity<?> findAllFeatures() {
		return new ResponseEntity<>(featureService.getAllFeatures(), HttpStatus.OK);
	}
	
	@GetMapping("menu")
	public ResponseEntity<?> menu() {
		return new ResponseEntity<>(featureService.menu(), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<Long> ids) {
		return new ResponseEntity<>(featureService.delete(ids), HttpStatus.OK);
	}
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<Long> ids) {
		return new ResponseEntity<>(featureService.active(ids), HttpStatus.OK);
	}
}
