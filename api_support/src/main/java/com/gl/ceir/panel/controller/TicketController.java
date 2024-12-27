package com.gl.ceir.panel.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.TicketDto;
import com.gl.ceir.panel.dto.TicketNoteDto;
import com.gl.ceir.panel.dto.TicketRateDto;
import com.gl.ceir.panel.repository.remote.DownloaderRepositoryRemote;
import com.gl.ceir.panel.service.TicketService;

import feign.Response;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
	private final TicketService ticketService;
	private final DownloaderRepositoryRemote downloaderRepositoryRemote;  

	@PostMapping("save")
	public ResponseEntity<?> save(@ModelAttribute TicketDto ticketDto) {
		return new ResponseEntity<>(ticketService.save(ticketDto), HttpStatus.OK);
	}
	
	@PostMapping("save/note")
	public ResponseEntity<?> savenote(@ModelAttribute TicketNoteDto ticketDto) {
		log.info("ticket: {}", ticketDto);
		return new ResponseEntity<>(ticketService.saveNote(ticketDto), HttpStatus.OK);
	}
	@PostMapping("save/rate")
	public ResponseEntity<?> saverate(@RequestBody TicketRateDto ticketDto) {
		log.info("ticket: {}", ticketDto);
		return new ResponseEntity<>(ticketService.saveRate(ticketDto),HttpStatus.OK);
	}

	@PostMapping("pagination")
	public ResponseEntity<?> permissions(@RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(ticketService.pagination(ulrd), HttpStatus.OK);
	}
	@PostMapping("{msisdn}/pagination")
	public ResponseEntity<?> permissions(@PathVariable String msisdn, @RequestBody PaginationRequestDto ulrd) {
		return new ResponseEntity<>(ticketService.paginationByMsisdn(ulrd,msisdn), HttpStatus.OK);
	}

	@GetMapping("/{ticketId}")
	public ResponseEntity<?> getById(@PathVariable String ticketId) {
		log.info("ticket id: {}", ticketId);
		return new ResponseEntity<>(ticketService.getById(ticketId), HttpStatus.OK);
	}
	@GetMapping("resolve/{ticketId}")
	public ResponseEntity<?> resolve(@PathVariable String ticketId) {
		log.info("ticket id: {}", ticketId);
		return new ResponseEntity<>(ticketService.resolve(ticketId), HttpStatus.OK);
	}
	
	@GetMapping("forgot-ticket/{msisdn}")
	public ResponseEntity<?> forgot(@PathVariable String msisdn) {
		log.info("msisdn: {}", msisdn);
		return new ResponseEntity<>(ticketService.forgotTicket(msisdn), HttpStatus.OK);
	}
	
	@GetMapping("verify-otp/{msisdn}/{otp}")
	public ResponseEntity<?> verifyopt(@PathVariable String msisdn, @PathVariable String otp) {
		log.info("msisdn: {},otp:{}", msisdn, otp);
		return new ResponseEntity<>(ticketService.verifyOtp(msisdn, otp), HttpStatus.OK);
	}
	
	@GetMapping("send-otp/{msisdn}")
	public ResponseEntity<?> sendotp(@PathVariable String msisdn) {
		log.info("msisdn: {}", msisdn);
		return new ResponseEntity<>(ticketService.sendotp(msisdn), HttpStatus.OK);
	}
	@GetMapping("dashboard")
	public ResponseEntity<?> dashboard() {
		return new ResponseEntity<>(ticketService.viewDashboard(), HttpStatus.OK);
	}
	@GetMapping("category")
	public ResponseEntity<?> categories() {
		return new ResponseEntity<>(ticketService.categories(), HttpStatus.OK);
	}
	@GetMapping("document/list")
	public ResponseEntity<?> documentlist() {
		return new ResponseEntity<>(ticketService.documentlist(), HttpStatus.OK);
	}
	
	@GetMapping("document/attachment/{id}")
	public Response attachment(@PathVariable String id) throws IOException {
		Response response = downloaderRepositoryRemote.attachment(id);
		log.info("response: ", response.body().asInputStream());
		return response;
	}
}
