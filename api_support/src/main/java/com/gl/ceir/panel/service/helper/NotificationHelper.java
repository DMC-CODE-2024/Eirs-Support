package com.gl.ceir.panel.service.helper;

import org.springframework.stereotype.Component;

import com.gl.ceir.panel.dto.NotificationDto;
import com.gl.ceir.panel.dto.response.TicketResponseDto;
import com.gl.ceir.panel.repository.remote.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
@Log4j2
public class NotificationHelper {
	private final NotificationRepository notificationRepository;
	public void send(NotificationDto notification) {
		try {
			log.info("Notification request:{}", notification);
			Object response = notificationRepository.sendemail(notification);
			log.info("Notification response:{}", response);
		}catch(Exception e) {
			log.error("Error while send notification:{}", e.getMessage());
		}
	}
}
