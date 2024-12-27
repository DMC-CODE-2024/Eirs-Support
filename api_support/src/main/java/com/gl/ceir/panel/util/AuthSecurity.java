package com.gl.ceir.panel.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.request.LoginRequest;
import com.gl.ceir.panel.dto.response.JwtResponse;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserFeatureIpAccessListEntity;
import com.gl.ceir.panel.entity.app.UserGroupEntity;
import com.gl.ceir.panel.repository.app.UserFeatureIpAccessListRepository;
import com.gl.ceir.panel.repository.app.UserGroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.JwtUtils;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.AclService;
import com.gl.ceir.panel.service.UserPasswordService;
import com.gl.ceir.panel.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@Log4j2
public class AuthSecurity {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final UserService userService;
	private final AclService aclService;
	private final UserGroupRepository userGroupRepository;
	private final UserFeatureIpAccessListRepository featureIpAccessListRepository;
	private final JwtUtils jwtUtils;
	private final UserPasswordService userPasswordService;
	private final Map<String, Integer> sessionMap;
	@Value("${eirs.allowed.failed.login.attempts:3}")
	private int allowedFailedLoginAttempts;
	@Value("${eirs.allowed.parallel.session:3}")
	private int allowedParallelSession;
	private List<String> notLoggedInList = Arrays.asList("0","4","5","21");
	private Map<String, String> statusmesage = new HashMap<String, String>();
	
	@PostConstruct
	private void init() {
		statusmesage.put("0", "inactive");
		statusmesage.put("3", "success");
		statusmesage.put("4", "suspended");
		statusmesage.put("5", "locked");
		statusmesage.put("21", "deleted");
	}
	
	public JwtResponse isSecurityPassed(LoginRequest loginRequest, HttpServletRequest request) {
		JwtResponse jwtResponse = JwtResponse.builder().apiResult("success").build();
		UserEntity entity = UserEntity.builder().build();
		this.isAllowedSessionLimitBreached(loginRequest, jwtResponse, request)
				.isLoggedIn(loginRequest, jwtResponse, request, entity)
				.isIpBlackListed(loginRequest, jwtResponse, request, entity)
				.isOutsideRegion(loginRequest, jwtResponse, request);
		return jwtResponse;
	}
	private AuthSecurity isAllowedSessionLimitBreached(LoginRequest loginRequest, JwtResponse jwtResponse, HttpServletRequest request) {
		Optional<UserEntity> ueo = userRepository.findByUserName(loginRequest.getUserName());
		if(ueo.isPresent()) {
			log.info("Parallel session:{}, current session:{}",  allowedParallelSession, ueo.get().getActiveSession());
			if( ueo.get().getActiveSession() >= allowedParallelSession) {
				log.warn("User:{} has breaked parallel session limit", loginRequest.getUserName());
				jwtResponse.setMessage("parallelSessionLimitBreached");
				jwtResponse.setApiResult("fail");
			}
		}
		return this;
	}
	
	private AuthSecurity isLoggedIn(LoginRequest loginRequest, JwtResponse jwtResponse, HttpServletRequest request, UserEntity entity) {
		try {
			if(jwtResponse.getApiResult().equals("fail")) return this;
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String jwt = jwtUtils.generateJwtToken(authentication);
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			Optional<UserEntity> ueo = userRepository.findByUserName(userDetails.getUsername());
			String message = "success";
			if(ueo.isPresent()) {
				log.info("Current status: {}, message:{}", ueo.get().getCurrentStatus(), statusmesage.get(ueo.get().getCurrentStatus()));
				message = statusmesage.get(ueo.get().getCurrentStatus());
				List<UserGroupEntity> ugroups = userGroupRepository.findByIdUserId(ueo.get().getId());
				message = CollectionUtils.isEmpty(ugroups) ? "groupNotAssigned": message;
				userRepository.save(ueo.get().toBuilder().lastLoginDate(LocalDateTime.now()).failedAttempt(0)
						.activeSession(ueo.get().getActiveSession() + 1).build());
				entity.setId(ueo.get().getId());
			}
			
			jwtResponse.setId(userDetails.getId());
			jwtResponse.setUserName(userDetails.getUsername());
			jwtResponse.setToken(jwt);
			jwtResponse.setEmail(userDetails.getEmail());
			jwtResponse.setMessage(message);
			jwtResponse.setApiResult(message);
			jwtResponse.setTemparoryPassword(userPasswordService.isItTemparoryPassword(entity));
			try{jwtResponse.setRemainingExpiryDays(ChronoUnit.DAYS.between(LocalDateTime.now(), entity.getPasswordDate()));}catch(Exception e) {}
			log.info("Response:{}", jwtResponse);
			sessionMap.put(userDetails.getUsername(), sessionMap.getOrDefault(request, 0) + 1);
		}catch(Exception e) {
			e.printStackTrace();
			jwtResponse.setMessage("invalidCredentials");
			jwtResponse.setApiResult("fail");
			this.isFailedAttemptReached(loginRequest, jwtResponse, request);
		}
		return this;
	}
	
	private AuthSecurity isOutsideRegion(LoginRequest loginRequest, JwtResponse jwtResponse, HttpServletRequest request) {
		log.info("Response:{}", jwtResponse);
		if(jwtResponse.getApiResult().equals("fail") == false && aclService.isAccessAllow(request.getRemoteAddr()) == false) {
			jwtResponse.setMessage("loginFromOutsideRegion");
			jwtResponse.setApiResult("fail");
		}
		return this;
	}
	private AuthSecurity isIpBlackListed(LoginRequest loginRequest, JwtResponse jwtResponse, HttpServletRequest request, UserEntity entity) {
		log.info("Logged in ip:{} for user id:{}", request.getRemoteAddr(), entity.getId());
		if(jwtResponse.getApiResult().equals("fail") == false) {
			List<UserFeatureIpAccessListEntity> list = featureIpAccessListRepository.findByUserId(entity.getId());
			if(CollectionUtils.isNotEmpty(list)) {
				if(list.stream().anyMatch(l -> l.getIpAddress().contains(request.getRemoteAddr()))) {
					log.info("Ip: {} blacklisted, so not to allow login", request.getRemoteAddr());
					jwtResponse.setMessage("ipNotWhitelisted");
					jwtResponse.setApiResult("fail");
				}
			}
		}
		return this;
	}
	private AuthSecurity isFailedAttemptReached(LoginRequest loginRequest, JwtResponse jwtResponse, HttpServletRequest request) {
		if(jwtResponse.getApiResult().equals("fail")) {
			jwtResponse.setApiResult("fail");
			Optional<UserEntity> oentity = userRepository.findByUserName(loginRequest.getUserName());
			if(oentity.isPresent() && oentity.get().getFailedAttempt() >= allowedFailedLoginAttempts) {
				userRepository.save(oentity.get().toBuilder().currentStatus(StatusEnum.DELETED.status).build());
				jwtResponse.setMessage("failedAttemptReached");
			}
		}
		return this;
	}
}
