package com.gl.ceir.panel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.TicketCategoryDto;
import com.gl.ceir.panel.service.TicketCategoryService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/ticket-category")
public class TicketCategoryController {
	private final TicketCategoryService ticketService;

	@PostMapping("save")
	public ResponseEntity<?> save(@RequestBody TicketCategoryDto ticketDto) {
		return new ResponseEntity<>(ticketService.save(ticketDto), HttpStatus.OK);
	}
	@GetMapping("list")
	public ResponseEntity<?> list() {
		return new ResponseEntity<>(ticketService.categories(), HttpStatus.OK);
	}
}
