package com.gl.ceir.panel.controller;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.constant.AccessEnum;
import com.gl.ceir.panel.dto.AclTreeDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.entity.app.RoleFeatureModuleAccessId;
import com.gl.ceir.panel.service.AclService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("acl")
@RequiredArgsConstructor
@Log4j2
public class AccessController {
	private final AclService aclService;
	
	@GetMapping("list")
	public @ResponseBody ResponseEntity<?> accessList() {
		
		return new ResponseEntity<>(Arrays.asList(AccessEnum.values()), HttpStatus.OK);
	}
	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody AclTreeDto aclDto , HttpServletRequest request) {
		return new ResponseEntity<>(aclService.save(aclDto, request), HttpStatus.OK);
	}
	@GetMapping("/findByRoleId/{roleId}")
	public ResponseEntity<?> getByRoleId(@PathVariable Long roleId){
		return new ResponseEntity<>(aclService.findByRoleId(roleId), HttpStatus.OK);
	}
	@GetMapping("/findTreeByRoleId/{roleId}")
	public ResponseEntity<?> getTreeByRoleId(@PathVariable Long roleId){
		return new ResponseEntity<>(aclService.getTreeByRoleId(roleId), HttpStatus.OK);
	}
	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(aclService.pagination(ulrd), HttpStatus.OK);
	}
	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<RoleFeatureModuleAccessId> ids) {
		return new ResponseEntity<>(aclService.delete(ids), HttpStatus.OK);
	}
	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<RoleFeatureModuleAccessId> ids) {
		return new ResponseEntity<>(aclService.active(ids), HttpStatus.OK);
	}
	@SuppressWarnings("rawtypes")
	@GetMapping("isAllowInYourRegion")
	public ResponseEntity<?> isAllowInYourRegion(HttpServletRequest request) {
		Enumeration headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
	        String headerName = (String)headerNames.nextElement();
	        log.info("header: " + headerName + ":" + request.getHeader(headerName));
	    }
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		log.info("Request ip:{}", request.getRemoteAddr());
		map.put("allow", aclService.isAccessAllow(request.getRemoteAddr()));
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
