package com.gl.ceir.panel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.constant.OtpChannelTypeEnum;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.dto.UserUpdateDto;
import com.gl.ceir.panel.service.RedmineBackendService;
import com.gl.ceir.panel.service.UserPermissionService;
import com.gl.ceir.panel.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final UserPermissionService userPermissionService;
	private final RedmineBackendService redmineBackendService;

	@Value("${user.for.testing:}")
	private String userForTesting;


	@PostMapping("save")
	public ResponseEntity<?> save(@Valid @ModelAttribute UserCreateDto ucd, HttpServletRequest request) {
		log.info("User create request:{}", ucd);
		return userService.save(ucd, request);
	}

	@RequestMapping(path = "/update/{id}", method = { RequestMethod.PUT, RequestMethod.POST }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> update(@Valid @ModelAttribute UserUpdateDto userDto, @PathVariable Long id,
			HttpServletRequest request) {
		return new ResponseEntity<>(userService.update(userDto, id, request), HttpStatus.OK);
	}

	@PostMapping("pagination")
	public ResponseEntity<?> pagination(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(userService.pagination(ulrd), HttpStatus.OK);
	}

	@RequestMapping("{id}")
	public ResponseEntity<?> view(@PathVariable Long id) {
		return new ResponseEntity<>(userService.viewById(id), HttpStatus.OK);
	}
	
	@RequestMapping("updateEmailAndMsisdn/{id}/{email}/{msisdn}")
	public ResponseEntity<?> updateEmailandMsisdn(@PathVariable Long id, @PathVariable String email, @PathVariable String msisdn) {
		return new ResponseEntity<>(userService.updateEmailAndMsisdn(id, email,msisdn), HttpStatus.OK);
	}

	@GetMapping("list")
	public ResponseEntity<?> findUsers() {
		return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
	}

	@GetMapping("permissions")
	public ResponseEntity<?> permissions() {
		return new ResponseEntity<>(userPermissionService.permissions().getPermissions(), HttpStatus.OK);
	}

	@GetMapping("isAlertForPasswordExpire")
	public ResponseEntity<?> isAlertForPasswordExpire() {
		return new ResponseEntity<>(userService.isAlertForPasswordExpire(), HttpStatus.OK);
	}

	@PostMapping("delete")
	public ResponseEntity<?> delete(@RequestBody List<Long> ids, HttpServletRequest request) {
		return new ResponseEntity<>(userService.delete(ids,request), HttpStatus.OK);
	}

	@PostMapping("active")
	public ResponseEntity<?> active(@RequestBody List<Long> ids, HttpServletRequest request) {
		return new ResponseEntity<>(userService.active(ids,request), HttpStatus.OK);
	}

	@PostMapping("reset-password")
	public ResponseEntity<?> resetpassword(@RequestBody List<Long> ids, HttpServletRequest request) {
		return userService.resetpassword(ids,request);
	}

	@GetMapping("getUserType")
	public ResponseEntity<?> getUserType(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		map.put("userType", userService.findUserType().type);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@GetMapping("findUsersByUserNameForTickets/{userName}")
	public ResponseEntity<?> getUserType(@PathVariable String userName) {
		userName = ObjectUtils.isNotEmpty(userForTesting) ? userForTesting : userName;
		log.info("Username:{}", userName);
		return userService.users(userName);
	}
	@GetMapping("send-otp/{otpchannel}/{emailormsisdn}")
	public ResponseEntity<?> sendotp(@PathVariable OtpChannelTypeEnum otpchannel,@PathVariable String emailormsisdn) {
		log.info("Otp request for:{}, receiver id:{}", otpchannel, emailormsisdn);
		return new ResponseEntity<>(userService.sendotp(otpchannel,emailormsisdn), HttpStatus.OK);
	}
	@GetMapping("verify-otp/{emailormsisdn}/{otp}")
	public ResponseEntity<?> sendotp(@PathVariable String emailormsisdn, @PathVariable String otp) {
		log.info("Otp request receiver id:{}",emailormsisdn);
		return new ResponseEntity<>(userService.verifyOtp(emailormsisdn, otp), HttpStatus.OK);
	}
	
	@RequestMapping("create-redmine-user")
	public ResponseEntity<?> createUserOnRedmine() {
		return new ResponseEntity<>(redmineBackendService.saveAllUser(), HttpStatus.OK);
	}
}
