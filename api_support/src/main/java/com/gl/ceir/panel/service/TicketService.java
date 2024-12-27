package com.gl.ceir.panel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gl.ceir.panel.dto.NotificationDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.TicketDto;
import com.gl.ceir.panel.dto.TicketNoteDto;
import com.gl.ceir.panel.dto.TicketRateDto;
import com.gl.ceir.panel.dto.response.ApiStatusDto;
import com.gl.ceir.panel.dto.response.AttachmentDetailsDto;
import com.gl.ceir.panel.dto.response.PaginationDto;
import com.gl.ceir.panel.dto.response.RedminUploadDto;
import com.gl.ceir.panel.dto.response.TicketResponseDto;
import com.gl.ceir.panel.entity.app.TicketEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.TicketRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.repository.remote.RedmineRemoteRepository;
import com.gl.ceir.panel.service.criteria.TicketCriteriaService;
import com.gl.ceir.panel.service.helper.NotificationHelper;
import com.gl.ceir.panel.service.helper.TicketHelper;
import com.gl.ceir.panel.util.OperatorUtil;
import com.gl.ceir.panel.util.OtpUtil;
import com.gl.ceir.panel.util.PlaceholderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class TicketService {
	private final UserRepository userRepository;
	private final TicketRepository ticketRepository;
	private final TicketCriteriaService ticketCriteriaService;
	private final RedmineRemoteRepository redmineRemoteRepository;
	private final TicketHelper ticketHelper;
	private final OtpUtil otpUtil;
	private final PlaceholderUtil placeholderUtil; 
	private final UserService userService;
	private final NotificationHelper notificationHelper;
	private final PassiveExpiringMap<String, String> otpMap;
	private final ObjectMapper objectMapper;
	private final OperatorUtil operatorUtil;
	@Value("${eirs.panel.register.client.type:REGISTERED}")
	private String loggedInUserType;
	@Value("${eirs.ticket.otp.message:Please use this otp: {otp} to access the tickets}")
	private String otpMessage;
	@Value("${eirs.ticket.create.sms:Your Ticket registered successfully with the Ticket id %s}")
	private String ticketCreateSms;
	@Value("${eirs.ticket.create.email:}")
	private String ticketCreateEmail;
	@Value("${eirs.ticket.create.email.subject:}")
	private String ticketCreateEmailSubject;
	@Value("${eirs.ticket.link:}")
	private String ticketLink;
	@Value("${eirs.panel.unregister.client.type:END_USER}")
	private String loggedOutUserType;
	
	@Value("${eirs.sms.channel.type:SMS}")
	private String smsChannelType;
	@Value("${eirs.email.channel.type:EMAIL}")
	private String emailChannelType;
	
	@Value("${eirs.prmotional.sms.channel.type:SMS}")
	private String smsPromoChannelType;
	@Value("${eirs.promotional.email.channel.type:EMAIL}")
	private String emailPromoChannelType;
	
	@Value("${eirs.ticket.feature.name:TicketCreation}")
	private String ticketFeatureName;
	@Value("${eirs.ticket.document.list:ID}")
	private List<String> documentlist;
	@Value("${eirs.country.code:+855}")
	private String countryCode;
	
	@Value("${eirs.redmine.excluded.categories:}")
	private List<String> redmineExcludedCategories;
	
	public TicketResponseDto save(TicketDto ticketDto) {
		TicketResponseDto ticketResponse = TicketResponseDto.builder().build();
		UserEntity user = userService.getLoggedInUser();
		String profilemsisdn = ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getProfile())
				&& StringUtils.isNotEmpty(user.getProfile().getPhoneNo()) ? user.getProfile().getPhoneNo() : null;
		String msisdn = StringUtils.isEmpty(ticketDto.getMobileNumber())
				? StringUtils.isNotEmpty(profilemsisdn) ? profilemsisdn : null : ticketDto.getMobileNumber();
		String profileemail = ObjectUtils.isNotEmpty(user) && ObjectUtils.isNotEmpty(user.getProfile())
				&& StringUtils.isNotEmpty(user.getProfile().getEmail()) ? user.getProfile().getEmail() : null;
		String email = StringUtils.isEmpty(ticketDto.getEmailAddress())
				? StringUtils.isNotEmpty(profileemail) ? profileemail : null : ticketDto.getEmailAddress();
		try {
			log.info("Ticket creation request msisdn:{}, profile msisdn:{}, final msisdn:{}", ticketDto.getMobileNumber(), profilemsisdn, msisdn);
			log.info("Ticket creation request: {}", ticketDto);
			if(StringUtils.isNotEmpty(msisdn) && "NULL".equals(msisdn)==false) {
				String countrycode = countryCode.startsWith("+") ? countryCode.substring(1, countryCode.length()): countryCode; 
				msisdn = msisdn.startsWith(countrycode) ? msisdn : countrycode + msisdn;
				log.info("Mobile with country code:{}", msisdn);
				if (ObjectUtils.isNotEmpty(ticketDto.getDocuments())) {
					Map<String, String> documentmap = objectMapper.readValue(ticketDto.getFileWithDocuments(), new TypeReference<HashMap<String,String>>() {});
					log.info("documentmap: {}", documentmap);
					List<RedminUploadDto> attachments = new ArrayList<RedminUploadDto>();
					for(MultipartFile multipart: ticketDto.getDocuments()) {
						Map<String, RedminUploadDto> upload = redmineRemoteRepository.upload(Arrays.asList(multipart), ticketHelper.toRedmineHeader(ticketDto));
						log.info("File:{},Content type:{}, response:{}", multipart.getOriginalFilename(), multipart.getContentType(), upload);
						RedminUploadDto rud = RedminUploadDto.builder().token(upload.get("upload").getToken())
								.filename(multipart.getOriginalFilename()).contentType(multipart.getContentType())
								.description(documentmap.get(multipart.getOriginalFilename())).build();
						log.info("Upload object document type:{}", documentmap.get(multipart.getOriginalFilename()));
						log.info("Upload object:{}", rud);
						attachments.add(rud);
					}
					ticketDto.setDocuments(null);
					ticketDto.setAttachments(attachments);
				}
				ticketDto.setPrivate(ticketDto.isResolved());
				ticketDto = ticketHelper.toRedmineDto(ticketDto);
				ticketDto.setMobileNumber(msisdn);
				ticketDto.setEmailAddress(email);
				log.info("Ticket creation payload for api: {}", ticketDto);
				ticketResponse = redmineRemoteRepository.createTicket(ticketDto, ticketHelper.toRedmineHeader(ticketDto));
				if (ticketDto.isResolved()) {
					log.info("private note:{}", ticketDto.isPrivateNotes());
					redmineRemoteRepository.rosolve("RESOLVED", ticketResponse.getTicketId(), ticketHelper.toRedmineHeader(
							TicketDto.builder().mobileNumber(ticketDto.getUserId()).userType(ticketDto.getUserType()).build()));
				}
				Map<String, String> values = new HashMap<String, String>();
				values.put("ticketId", ticketResponse.getTicketId());
				values.put("mobile", msisdn);
				values.put("email", email);
				values.put("firstName", ticketDto.getFirstName());
				values.put("lastName", StringUtils.isNotEmpty(ticketDto.getLastName()) ? ticketDto.getLastName(): "");
				values.put("email", StringUtils.isNotEmpty(ticketDto.getEmailAddress()) ? ticketDto.getEmailAddress(): "");
				values.put("link", placeholderUtil.message(values, ticketLink));
				
				NotificationDto smsrequest = NotificationDto.builder().channelType(smsPromoChannelType)
						.message(placeholderUtil.message(values, ticketCreateSms)).userId(ObjectUtils.isNotEmpty(user) ? user.getId(): null)
						.operatorName(operatorUtil.getOperator(msisdn)).msisdn(msisdn).featureId(null)
						.featureName(ticketFeatureName).featureTxnId(ticketResponse.getTicketId()).build();
				
				NotificationDto emailrequest = NotificationDto.builder().channelType(emailPromoChannelType)
						.message(placeholderUtil.message(values, ticketCreateEmail))
						.subject(placeholderUtil.message(values, ticketCreateEmailSubject))
						.featureName(ticketFeatureName).email(ticketDto.getEmailAddress()).featureId(null)
						.featureTxnId(ticketResponse.getTicketId()).userId(ObjectUtils.isNotEmpty(user) ? user.getId(): null).build();
				if(ObjectUtils.isNotEmpty(msisdn))this.sendnotification(smsrequest);
				if(ObjectUtils.isNotEmpty(ticketDto.getEmailAddress()))this.sendnotification(emailrequest);
				ticketResponse = ticketResponse.toBuilder().message("registerTicketSuccess").build();
			} else {
				log.warn("Msisdn:{} not valid for ticket, so ticket will not be created", msisdn);
				ticketResponse = ticketResponse.toBuilder().message("registerTicketFailed").build();
			}
		}catch(Exception e) {
			log.error("Error while save:{} ticket", e.getMessage());
			ticketResponse = ticketResponse.toBuilder().message("registerTicketFailed").build();
		}
		log.info("Redmin response:{}", ticketResponse);
		return ticketResponse;
	}
	
	@Async
	private void sendnotification(NotificationDto notification) {
		try {
			notificationHelper.send(notification);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public PaginationDto pagination(PaginationRequestDto ulrd) {
		log.info("Request: {}", ulrd);
		Map<String, String> params = ticketHelper.toPaginationDto(ulrd);
		log.info("Params: {}", params);
		String mobileNumber = ObjectUtils.isNotEmpty(params.get("mobileNumber")) ? params.get("mobileNumber")
				: ObjectUtils.isNotEmpty(params.get("contactNumber")) ? params.get("contactNumber") : null;
		HttpHeaders headers = ticketHelper.toRedmineHeader(TicketDto.builder().build());
		headers.add("loggedInUser", params.get("loggedInUser"));
		log.info("is my dashboard:{}", Boolean.valueOf(params.get("isMyDashboard")));
		
		
		return redmineRemoteRepository.pagination(params.get("startDate"), params.get("endDate"),
				params.get("ticketId"), mobileNumber, params.get("ticketStatus"), params.get("clientType"),
				params.get("raisedBy"), params.get("loggedInUser"), Boolean.valueOf(params.get("isMyDashboard")),
				ulrd.getPage().getCurrent() - 1, ulrd.getPage().getSize(), headers);
	}

	public PaginationDto paginationByMsisdn(PaginationRequestDto ulrd, String msisdn) {
		log.info("Request: {}", ulrd);
		Map<String, String> params = ticketHelper.toPaginationDto(ulrd);
		log.info("Params by msisdn:{},msisdn:{}", params, msisdn);
		HttpHeaders headers = ticketHelper.toRedmineHeader(TicketDto.builder().build());
		return redmineRemoteRepository.ticketByMsisdn(msisdn, ulrd.getPage().getCurrent() - 1, ulrd.getPage().getSize(),
				headers);
	}

	public ApiStatusDto getById(String id) {
		log.info("ticket id: {}", id);
		try {
			TicketResponseDto ticketDto  = redmineRemoteRepository.view(id,ticketHelper.toRedmineHeader(TicketDto.builder().build()));
			this.setAttachment(ticketDto);
			log.info("ticket dto: {}", ticketDto);
			return ApiStatusDto.builder().message(ObjectUtils.isEmpty(ticketDto) ? "notValidTicketId" : "").data(ticketDto).build();
		} catch (Exception e) {
			log.error("Ticket id:{}, not exist:{}", id, e.getMessage());
		}
		return ApiStatusDto.builder().message("notValidTicketId").data(null).build();
	}
	
	private void setAttachment(TicketResponseDto ticketDto) {
		try {
			ticketDto.getIssue().getJournals().forEach(j -> {
				List<AttachmentDetailsDto> details = new ArrayList<AttachmentDetailsDto>();
				j.getDetails().forEach(d -> {
					Optional<RedminUploadDto> ou = ticketDto.getIssue().getUploads().stream().filter(u -> d.getName().equals(String.valueOf(u.getId()))).findFirst();
					if(ou.isPresent() && ou.get().getFilesize() > 0) {
						d.setContentType(ou.get().getContentType());
						d.setFilename(ou.get().getFilename());
						d.setFilesize(ou.get().getFilesize());
						d.setContentUrl(ou.get().getContentUrl());
						details.add(d);
					}
				});
				j.setDetails(details);
			});
		}catch(Exception e) {
			log.error("Error:{} while add attachment for ticket:{}", e.getMessage(), ticketDto.getTicketId());
		}
	}

	public List<TicketEntity> getByMobileNumber(String mobileNumber) {
		return ticketRepository.findByMobileNumber(mobileNumber);
	}

	public TicketResponseDto saveNote(TicketNoteDto note) {
		log.info("ticket note: {}", note);
		UserEntity entity = userService.getLoggedInUser();
		TicketResponseDto ticket = redmineRemoteRepository.view(note.getTicketId(),ticketHelper.toRedmineHeader(TicketDto.builder().build()));
		HttpHeaders headers = ticketHelper.toRedmineHeader(TicketDto.builder().mobileNumber(ticket.getUserId()).userType(ticket.getUserType()).build());
		log.info("Ticket:{}", ticket);
		log.info("Ticket:{},headers:{}",ticket.getTicketId(), headers);
		if (ObjectUtils.isNotEmpty(note.getDocuments())) {
			try {
				Map<String, String> documentmap = objectMapper.readValue(note.getFileWithDocuments(), new TypeReference<HashMap<String,String>>() {});
				List<RedminUploadDto> attachments = new ArrayList<RedminUploadDto>();
				for(MultipartFile multipart: note.getDocuments()) {
					Map<String, RedminUploadDto> upload = redmineRemoteRepository.upload(Arrays.asList(multipart), headers);
					log.info("File:{},Content type:{}, response:{}", multipart.getOriginalFilename(), multipart.getContentType(), upload);
					attachments.add(RedminUploadDto.builder().token(upload.get("upload").getToken())
							.filename(multipart.getOriginalFilename())
							.description(documentmap.get(multipart.getOriginalFilename()))
							.contentType(multipart.getContentType()).build());
				}
				note.setAttachments(attachments);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		log.info("Comments: {}", note);
		note.setDocuments(null);
		redmineRemoteRepository.note(note.getTicketId(), note, headers);
		TicketResponseDto ticketDto = redmineRemoteRepository.view(note.getTicketId(), headers);
		this.setAttachment(ticketDto);
		return ticketDto;
	}

	public String saveRate(TicketRateDto rate) {
		UserEntity entity = userService.getLoggedInUser();
		TicketResponseDto ticket = redmineRemoteRepository.view(rate.getTicketId(),
				ticketHelper.toRedmineHeader(TicketDto.builder().build()));
		HttpHeaders headers = ticketHelper.toRedmineHeader(
				TicketDto.builder().mobileNumber(ticket.getUserId()).userType(ticket.getUserType()).build());
		log.info("Ticket:{},headers:{}",ticket.getTicketId(), headers);
		String result = redmineRemoteRepository.rate(rate.getTicketId(), rate, headers);
		log.info("ticket rate: {}, result:{}", rate, result);
		return result;
	}

	public TicketResponseDto resolve(String ticketId) {
		UserEntity entity = userService.getLoggedInUser();
		TicketResponseDto ticket = redmineRemoteRepository.view(ticketId,
				ticketHelper.toRedmineHeader(TicketDto.builder().build()));
		HttpHeaders headers = ticketHelper.toRedmineHeader(
				TicketDto.builder().mobileNumber(ticket.getUserId()).userType(ticket.getUserType()).build());
		if(ObjectUtils.isEmpty(entity)) {
			headers.remove("X-Client-Id");
			headers.add("X-Client-Id", ticket.getUserId());
		}
		log.info("Ticket:{},headers:{}",ticketId, headers);
		redmineRemoteRepository.rosolve("RESOLVED", ticketId, headers);
		return redmineRemoteRepository.view(ticketId,headers);
	}

	public TicketResponseDto forgotTicket(String msisdn) {
		return null;
	}

	public ApiStatusDto sendotp(String msisdn) {
		try {
			UserEntity user = userService.getLoggedInUser();
			String otp = otpUtil.phoneOtp(msisdn);
			Map<String, String> values = new HashMap<String, String>();
			values.put("otp", otp);
			NotificationDto request = NotificationDto.builder().channelType(smsChannelType)
					.message(placeholderUtil.message(values, otpMessage)).featureName(ticketFeatureName).msisdn(msisdn)
					.featureTxnId(String.valueOf(System.currentTimeMillis()))
					.operatorName(operatorUtil.getOperator(msisdn)).featureId(null)
					.userId(ObjectUtils.isNotEmpty(user) ? user.getId() : null).build();
			log.info("Generated otp:{}, for msisdn:{},request:{}", otp, msisdn, request);
			notificationHelper.send(request);
			otpMap.put(msisdn, otp);
			log.info("Otp map size:{}", otpMap.size());
			return ApiStatusDto.builder().message("sendOtpSuccess").build();
		}catch(Exception e) {
			e.printStackTrace();
			return ApiStatusDto.builder().message("sendOtpFailed").build();
		}
	}

	public ApiStatusDto verifyOtp(String msisdn, String otp) {
		PaginationDto tickets = PaginationDto.builder().build();
		try {
			tickets = redmineRemoteRepository.ticketByMsisdn(msisdn,0,10,
				ticketHelper.toRedmineHeader(TicketDto.builder().mobileNumber("DUMMY_AGENT").build()));
			String sentotp = otpMap.get(msisdn);
			log.info("Recieved otp:{}, for msisdn:{},size:{},cache otp:{},otp map size:{}", otp, msisdn, tickets.getContent().size(),sentotp, otpMap.size());
			return ObjectUtils.isNotEmpty(sentotp) && otp.equals(sentotp)
					? ApiStatusDto.builder().message("verifyOtpSuccess").size(tickets.getContent().size())
							.id(CollectionUtils.isNotEmpty(tickets.getContent()) ? tickets.getContent().get(0).getTicketId() : "").build()
					: ApiStatusDto.builder().message("verifyOtpFailed").size(tickets.getContent().size())
							.id(CollectionUtils.isNotEmpty(tickets.getContent()) ? tickets.getContent().get(0).getTicketId() : "").build();
		}catch(Exception e) {
			log.error("Error while fetching ticket by msisdn during verify otp:{}", e.getMessage());
			return ApiStatusDto.builder().message("verifyOtpFailed").size(tickets.getContent().size()).build();
		}
		
	}

	public JsonNode viewDashboard() {
		HttpHeaders headers = ticketHelper.toRedmineHeader(TicketDto.builder().build());
		headers.remove("X-Client-Type");
		headers.add("X-Client-Type", loggedInUserType);
		return redmineRemoteRepository.dashboard(headers);
	}

	public JsonNode categories() {
		ArrayNode onode = objectMapper.createArrayNode();
		try {
			HttpHeaders headers = ticketHelper.toRedmineHeader(TicketDto.builder().mobileNumber("DUMMY_AGENT").build());
			headers.remove("X-Client-Type");
			headers.add("X-Client-Type", loggedOutUserType);
			JsonNode jsonNode = redmineRemoteRepository.categories(headers);
			ArrayNode arrayNode = (ArrayNode) jsonNode;
			Iterator<JsonNode> node = arrayNode.elements();
			while (node.hasNext()) {
				JsonNode jnode = node.next();
				if(redmineExcludedCategories.contains(jnode.get("name").asText())==false) {
					ObjectNode inode = objectMapper.createObjectNode();
					inode.put("name",  jnode.get("name").asText());
		    		inode.put("id",  jnode.get("id").asText());
					onode.add(inode);
				}
			}
		}catch(Exception e) {
			log.error("Error:{} while fetching redmine categories", e.getMessage());
		}
		return onode;
	}
	public List<String> documentlist(){
		return documentlist;
	}
}
