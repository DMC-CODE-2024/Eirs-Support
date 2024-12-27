package com.gl.ceir.panel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserGroupDto;
import com.gl.ceir.panel.entity.app.UserGroupId;
import com.gl.ceir.panel.service.UserGroupService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("userGroup")
@RequiredArgsConstructor
@Log4j2
public class UserGroupController {
	private final UserGroupService userGroupService;
	
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody UserGroupDto userGroupDto, HttpServletRequest request) {
		log.info("Going to save user group");
		return new ResponseEntity<>(userGroupService.save(userGroupDto,request), HttpStatus.OK);
	}
	@PostMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody UserGroupDto userRoleDto, @PathVariable Long id, HttpServletRequest request) {
		return new ResponseEntity<>(userGroupService.update(userRoleDto, id,request), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		log.info("pagination:{}", ulrd);
		return new ResponseEntity<>(userGroupService.pagination(ulrd), HttpStatus.OK);
	}
	@GetMapping("list")
	public ResponseEntity<?> list() {
		return new ResponseEntity<>(userGroupService.list(), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<UserGroupId> ids, HttpServletRequest request) {
		return new ResponseEntity<>(userGroupService.delete(ids,request), HttpStatus.OK);
	} 
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<UserGroupId> ids) {
		return new ResponseEntity<>(userGroupService.active(ids), HttpStatus.OK);
	}
	@GetMapping("/{userId}")
	public ResponseEntity<?> getById(@PathVariable Long userId){
		return new ResponseEntity<>(userGroupService.getById(userId), HttpStatus.OK);
	}
	@PostMapping("findByUserIds")
	public ResponseEntity<?> getByUserIds(@RequestBody List<Long> userIds){
		return new ResponseEntity<>(userGroupService.getByUserIdsId(userIds), HttpStatus.OK);
	}
}
