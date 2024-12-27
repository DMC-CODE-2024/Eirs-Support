package com.gl.ceir.panel.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.dto.TicketCategoryDto;
import com.gl.ceir.panel.entity.app.TicketCategoryEntity;
import com.gl.ceir.panel.repository.app.TicketCategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class TicketCategoryService {
	private final TicketCategoryRepository ticketCategoryRepository;
	
	public TicketCategoryEntity save(TicketCategoryDto dto) {
		TicketCategoryEntity tcd = TicketCategoryEntity.builder().build();
		BeanUtils.copyProperties(dto, tcd);
		return ticketCategoryRepository.save(tcd);
	}
	public List<TicketCategoryEntity> categories() {
		return ticketCategoryRepository.findAll();
	}
}
