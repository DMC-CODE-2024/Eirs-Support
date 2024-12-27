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

import com.gl.ceir.panel.dto.GroupRoleDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.entity.app.GroupRoleId;
import com.gl.ceir.panel.service.GroupRoleService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("groupRole")
@RequiredArgsConstructor
public class GroupRoleController {
	private final GroupRoleService groupRoleService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody GroupRoleDto groupRoleDto, HttpServletRequest request) {
		return new ResponseEntity<>(groupRoleService.save(groupRoleDto,request), HttpStatus.OK);
	}
	@GetMapping("/{groupId}")
	public ResponseEntity<?> getById(@PathVariable Long groupId){
		return new ResponseEntity<>(groupRoleService.getById(groupId), HttpStatus.OK);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody GroupRoleDto groupRoleDto, @PathVariable Long id, HttpServletRequest request) {
		log.info("Group:{},update request:{}", id, groupRoleDto);
		return new ResponseEntity<>(groupRoleService.update(groupRoleDto, id,request), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<GroupRoleId> ids) {
		return new ResponseEntity<>(groupRoleService.delete(ids), HttpStatus.OK);
	}
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<GroupRoleId> ids) {
		return new ResponseEntity<>(groupRoleService.active(ids), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(groupRoleService.pagination(ulrd), HttpStatus.OK);
	}
}
