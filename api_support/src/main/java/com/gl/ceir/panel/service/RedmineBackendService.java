package com.gl.ceir.panel.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.dto.RedmineBackendResponse;
import com.gl.ceir.panel.dto.RedmineUserDto;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.UserGroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.repository.remote.RedmineBackendRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedmineBackendService {
	private final HttpHeaders toRedmineBackendHeader;
	private final RedmineBackendRepository redmineBackendRepository;
	private final UserRepository userRepository;
	private final UserGroupRepository userGroupRepository;

	public boolean saveAllUser() {
		boolean created = false;
		try {
			userGroupRepository.findAll().stream().forEach(u -> {
				try {
					this.saveUser(u.getUser().getUserName());
				} catch (Exception e) {
					log.info("Error:{} while create user:{} in redmine", e.getMessage(), u.getUser().getUserName());
				}
			});
			created = true;
		} catch (Exception e) {
			log.info("Error while create users:{} in redmine", e.getMessage());
		}
		return created;
	}

	public boolean saveUser(String username) {
		boolean created = false;
		try {
			log.info("Going to create redmine user:{}", username);
			UserEntity user = userRepository.findByUserName(username).orElse(null);
			RedmineBackendResponse redmine = this.getRedmineUser(username);
			if (ObjectUtils.isNotEmpty(user) && ObjectUtils.isEmpty(redmine) && CollectionUtils.isNotEmpty(redmine.getUsers())) {
				RedmineUserDto ruser = RedmineUserDto.builder().admin(true).firstname(user.getProfile().getFirstName())
						.lastname(user.getProfile().getLastName()).login(user.getUserName())
						.mail(user.getProfile().getEmail()).password(username).build();
				RedmineBackendResponse request = RedmineBackendResponse.builder().user(ruser).build();
				log.info("Redmine backend user request:{}", request);
				RedmineBackendResponse response = redmineBackendRepository.save(request, toRedmineBackendHeader);
				log.info("Redmine backend user response:{}", response);
				created = true;
			} else {
				log.info("Redmine user:{}", redmine);
				RedmineUserDto ruser = RedmineUserDto.builder().firstname(user.getProfile().getFirstName())
						.lastname(user.getProfile().getLastName()).mail(user.getProfile().getEmail()).build();
				RedmineBackendResponse request = RedmineBackendResponse.builder().user(ruser).build();
				log.info("Redmine backend user request:{}", request);
				redmineBackendRepository.update(redmine.getUsers().get(0).getId(),request, toRedmineBackendHeader);
			}
		} catch (Exception e) {
			log.info("Error:{} while create user:{} in redmine", e.getMessage(), username);
		}
		return created;
	}
	
	public RedmineBackendResponse getRedmineUser(String username) {
		RedmineBackendResponse response = redmineBackendRepository.view(username, toRedmineBackendHeader);
		return response;
	}
	
	public boolean isUserExist(String username) {
		boolean exist = false;
		log.info("Going to check redmine user existence:{}", username);
		RedmineBackendResponse response = redmineBackendRepository.view(username, toRedmineBackendHeader);
		if (ObjectUtils.isNotEmpty(response) && CollectionUtils.isNotEmpty(response.getUsers())) {
			exist = response.getUsers().stream().filter(u -> username.equals(u.getLogin())).findAny().isPresent();
		}
		log.info("User:{} is exist in redmine backend:{}", username, exist);
		return exist;
	}
}
