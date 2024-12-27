package com.gl.ceir.panel.service;

import org.springframework.stereotype.Service;

import com.gl.ceir.panel.dto.AuditTrailDto;
import com.gl.ceir.panel.repository.remote.NotificationRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class AuditService {
	private final NotificationRepository notificationRepository;
	
	public void audit(HttpServletRequest request, AuditTrailDto audit) {
		
	}
}
