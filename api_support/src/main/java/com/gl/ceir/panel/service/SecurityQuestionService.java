package com.gl.ceir.panel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gl.ceir.panel.entity.app.SecurityQuestionEntity;
import com.gl.ceir.panel.repository.app.SecurityQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor()
public class SecurityQuestionService {
	
	private final SecurityQuestionRepository securityQuestionRepository;
	public List<SecurityQuestionEntity> list(){
		return securityQuestionRepository.findAll();
	}
}
