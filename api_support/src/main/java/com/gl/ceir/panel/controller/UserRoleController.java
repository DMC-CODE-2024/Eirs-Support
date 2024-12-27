package com.gl.ceir.panel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserRoleDto;
import com.gl.ceir.panel.entity.app.UserRoleId;
import com.gl.ceir.panel.service.UserRoleService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("userRole")
@RequiredArgsConstructor
public class UserRoleController {
	private final UserRoleService userRoleService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody UserRoleDto userGroupDto, HttpServletRequest request) {
		return new ResponseEntity<>(userRoleService.save(userGroupDto,request), HttpStatus.OK);
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody UserRoleDto userRoleDto, @PathVariable Long id, HttpServletRequest request) {
		return new ResponseEntity<>(userRoleService.update(userRoleDto, id,request), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(userRoleService.pagination(ulrd), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<UserRoleId> ids, HttpServletRequest request) {
		return new ResponseEntity<>(userRoleService.delete(ids,request), HttpStatus.OK);
	}
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<UserRoleId> ids) {
		return new ResponseEntity<>(userRoleService.active(ids), HttpStatus.OK);
	}
	@RequestMapping("{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {
		return new ResponseEntity<>(userRoleService.getById(id), HttpStatus.OK);
	}
}
