package com.gl.ceir.panel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.RoleDto;
import com.gl.ceir.panel.entity.app.RoleEntity;
import com.gl.ceir.panel.entity.audit.AuditTrailEntity;
import com.gl.ceir.panel.repository.audit.AuditTrailRepository;
import com.gl.ceir.panel.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
	private final RoleService roleService;
	private final AuditTrailRepository auditTrailRepository;

	@PostMapping("save")
	public @ResponseBody RoleEntity save(@RequestBody RoleDto roleDto, HttpServletRequest request) {
		return roleService.save(roleDto, request);
	}

	@GetMapping("list")
	public ResponseEntity<?> findRoles() {
		return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public @ResponseBody RoleEntity getById(@PathVariable Long id) {
		return roleService.getById(id);
	}

	@PutMapping("/update/{id}")
	public @ResponseBody RoleEntity update(@RequestBody RoleDto roleDto, @PathVariable Long id,
			HttpServletRequest request) {
		log.info("Role:{},update request:{}", id, roleDto);
		return roleService.update(roleDto, id, request);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody RoleEntity delete(@PathVariable Long id, HttpServletRequest request) {
		return roleService.deleteById(id, request);
	}

	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(roleService.pagination(ulrd), HttpStatus.OK);
	}

	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<Long> ids) {
		return new ResponseEntity<>(roleService.delete(ids), HttpStatus.OK);
	}

	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<Long> ids) {
		return new ResponseEntity<>(roleService.active(ids), HttpStatus.OK);
	}

	@GetMapping("saveaudit")
	public ResponseEntity<?> saveaudit() {
		AuditTrailEntity audit = AuditTrailEntity.builder().featureId(1l).featureName("demo").publicIp("demo")
				.roleType("demo").subFeature("semo").txnId(String.valueOf(System.currentTimeMillis())).userId(1l)
				.userName("demo").details("details").build();
		auditTrailRepository.save(audit);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
