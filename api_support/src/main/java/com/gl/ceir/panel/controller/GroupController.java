package com.gl.ceir.panel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.GroupDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.service.GroupService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("group")
public class GroupController {
	@Autowired
	private GroupService groupService;

	@PostMapping("save")
	public @ResponseBody GroupEntity save(@RequestBody GroupDto groupDto, HttpServletRequest request) {
		return groupService.save(groupDto, request);
	}

	@GetMapping("parents")
	public ResponseEntity<?> findParents() {
		return new ResponseEntity<>(groupService.getParents(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public @ResponseBody GroupEntity getById(Authentication authentication, @PathVariable Long id) {
		log.info("user name: {}", authentication.getName());
		return groupService.getById(id);
	}

	@PutMapping("/update/{id}")
	public @ResponseBody GroupEntity update(@RequestBody GroupDto groupDto, @PathVariable Long id, HttpServletRequest request) {
		log.info("Group:{},update request:{}", id, groupDto);
		return groupService.update(groupDto, id, request);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody GroupEntity delete(@PathVariable Long id, HttpServletRequest request) {
		return groupService.deleteById(id,request);
	}

	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(groupService.pagination(ulrd), HttpStatus.OK);
	}

	@GetMapping("list")
	public ResponseEntity<?> findUsers() {
		return new ResponseEntity<>(groupService.getGroups(), HttpStatus.OK);
	}
}
