package com.gl.ceir.panel.controller;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.ChangePasswordDto;
import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.dto.request.LoginRequest;
import com.gl.ceir.panel.dto.request.SignupRequest;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.UserGroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.JwtUtils;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.AclService;
import com.gl.ceir.panel.service.UserService;
import com.gl.ceir.panel.util.AuthSecurity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.jodah.expiringmap.ExpiringMap;

@CrossOrigin(origins = "http://localhost:4200")
@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;
	private final JwtUtils jwtUtils;
	private final UserService userService;
	private final UserGroupRepository userGroupRepository;
	private final AclService aclService;
	private final AuthSecurity authSecurity;
	private final ExpiringMap<String, String> session;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		return ResponseEntity.ok(authSecurity.isSecurityPassed(loginRequest, request));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,HttpServletRequest request) {
		UserCreateDto ucd = UserCreateDto.builder().build();
		BeanUtils.copyProperties(signUpRequest, ucd);
		return userService.save(ucd,request);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<?> emailExist(@PathVariable String email) {
		return new ResponseEntity<>(userRepository.existsByProfileEmail(email), HttpStatus.OK);
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto cpd,HttpServletRequest request) {
		return new ResponseEntity<>(userService.changePassword(cpd,request), HttpStatus.OK);
	}
	@GetMapping("/isLogin")
	public ResponseEntity<?> isLogin() {
		return userService.prevalidate();
	}
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader("Authorization") String token) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		token = token.substring("Bearer ".length(), token.length());
		session.setExpiration(token, 2, TimeUnit.SECONDS);
        if (authentication != null) {
        	new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
