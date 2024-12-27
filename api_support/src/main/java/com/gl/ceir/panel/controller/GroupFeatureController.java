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

import com.gl.ceir.panel.dto.GroupFeatureDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.entity.app.GroupFeatureId;
import com.gl.ceir.panel.service.GroupFeatureService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("groupFeature")
@RequiredArgsConstructor
public class GroupFeatureController {
	private final GroupFeatureService groupFeatureService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody GroupFeatureDto groupFeatureDto, HttpServletRequest request) {
		return new ResponseEntity<>(groupFeatureService.save(groupFeatureDto,request), HttpStatus.OK);
	}
	@GetMapping("/{groupId}")
	public ResponseEntity<?> getById(@PathVariable Long groupId){
		log.info("Group id:{}", groupId);
		return new ResponseEntity<>(groupFeatureService.getById(groupId), HttpStatus.OK);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody GroupFeatureDto groupFeatureDto, @PathVariable Long id, HttpServletRequest request) {
		log.info("Group:{},update request:{}", id, groupFeatureDto);
		return new ResponseEntity<>(groupFeatureService.update(groupFeatureDto, id,request), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<GroupFeatureId> ids) {
		return new ResponseEntity<>(groupFeatureService.delete(ids), HttpStatus.OK);
	}
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<GroupFeatureId> ids) {
		return new ResponseEntity<>(groupFeatureService.active(ids), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(groupFeatureService.pagination(ulrd), HttpStatus.OK);
	}
}
