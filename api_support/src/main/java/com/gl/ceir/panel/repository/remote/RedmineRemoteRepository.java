package com.gl.ceir.panel.repository.remote;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.gl.ceir.panel.config.MultipartSupportConfig;
import com.gl.ceir.panel.dto.TicketDto;
import com.gl.ceir.panel.dto.TicketNoteDto;
import com.gl.ceir.panel.dto.TicketRateDto;
import com.gl.ceir.panel.dto.response.PaginationDto;
import com.gl.ceir.panel.dto.response.RedminUploadDto;
import com.gl.ceir.panel.dto.response.TicketResponseDto;

@Service
@FeignClient(url = "${eirs.redmine.backend.url}", value = "redmineRepository", configuration = MultipartSupportConfig.class)
public interface RedmineRemoteRepository {
	@RequestMapping(value = "/ticket", method = RequestMethod.POST)
	public TicketResponseDto createTicket(@RequestBody TicketDto ticketDto, @RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket", method = RequestMethod.GET)
	public PaginationDto pagination(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate, @RequestParam(value = "ticketId") String ticketId,
			@RequestParam(name = "contactNumber") String contactNumber, @RequestParam(name = "status") String status,
			@RequestParam(name = "clientType") String clientType, @RequestParam(name = "raisedBy") String raisedBy,
			@RequestParam(name = "loggedInUser") String loggedInUser,
			@RequestParam(name = "isMyDashboard") boolean isMyDashboard,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size, @RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket/{ticketId}", method = RequestMethod.GET)
	public TicketResponseDto view(@PathVariable(value = "ticketId") String ticketId,
			@RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket/{ticketId}/notes", method = RequestMethod.PUT)
	public TicketResponseDto note(@PathVariable(value = "ticketId") String ticketId, @RequestBody TicketNoteDto note,
			@RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket/{status}/{ticketId}", method = RequestMethod.PUT)
	public Object rosolve(@PathVariable(value = "status") String status,
			@PathVariable(value = "ticketId") String ticketId, @RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket/attachment/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, RedminUploadDto> upload(@RequestPart(value = "file") List<MultipartFile> file,
			@RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket/msisdn/{msisdn}", method = RequestMethod.GET)
	public PaginationDto ticketByMsisdn(@PathVariable(value = "msisdn") String msisdn,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size, @RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public JsonNode dashboard(@RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/projects/issue_categories", method = RequestMethod.GET)
	public JsonNode categories(@RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/ticket/{ticketId}/rate", method = RequestMethod.POST)
	public String rate(@PathVariable(value = "ticketId") String ticketId, @RequestBody TicketRateDto rate,
			@RequestHeader HttpHeaders headers);

	@PostMapping(value = "/upload-file-error", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	String fileUploadError(@RequestPart(value = "file") MultipartFile file);
}
