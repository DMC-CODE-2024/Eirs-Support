package com.gl.ceir.panel.service.helper;

import java.time.LocalDate;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.gl.ceir.panel.constant.UserTypeEnum;
import com.gl.ceir.panel.dto.FilterDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.TicketDto;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.service.UserPermissionService;
import com.gl.ceir.panel.service.UserService;
import com.gl.ceir.panel.util.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class TicketHelper {
	private final UserService userService;
	@SuppressWarnings("unused")
	private final UserPermissionService permissionService;
	@Value("${eirs.register.client.type:REGISTERED}")
	private String loggedInUserType;
	@Value("${eirs.redmine.registered.user:DUMMY_AGENT}")
	private String redmineRegisteredUser;

	@Value("${eirs.unregister.client.type:END_USER}")
	private String loggedOutUserType;
	
	private final DateUtil dateUtil;
	
	
	@Value("${eirs.system.admin.support.group:ticket-support}")
	private String systemSupport;
	@Value("${eirs.customer.care.group:customer_care}")
	private String customerCare;
	
	public TicketDto toRedmineDto(TicketDto ticketDto) {
		UserEntity entity = userService.getLoggedInUser();
		if(ObjectUtils.isNotEmpty(entity)) {
			if(StringUtils.isEmpty(ticketDto.getFirstName()) && StringUtils.isEmpty(ticketDto.getMobileNumber())) {
				ticketDto = ticketDto.toBuilder().firstName(entity.getProfile().getFirstName())
						.lastName(entity.getProfile().getLastName()).mobileNumber(entity.getProfile().getPhoneNo())
						.emailAddress(entity.getProfile().getEmail()).referenceId(ticketDto.getReferenceId()).build();
			}
		}
		//String raisedBy = permissionService.findHighAccessLevelGroupId(entity);
		String raisedBy = ObjectUtils.isNotEmpty(entity) ? entity.getUserName() :  loggedOutUserType;
		log.info("Ticket raised by: {}", raisedBy);
		return ticketDto.toBuilder().firstName(ticketDto.getFirstName()).lastName(ticketDto.getLastName())
				.mobileNumber(ticketDto.getMobileNumber()).emailAddress(ticketDto.getEmailAddress()).category(ticketDto.getCategory())
				.subject(ticketDto.getSubject()).description(ticketDto.getDescription())
				.raisedBy(ObjectUtils.isEmpty(raisedBy) ? ObjectUtils.isNotEmpty(ticketDto.getEmailAddress()) ? ticketDto.getEmailAddress():ticketDto.getMobileNumber(): raisedBy).build();
	}

	public HttpHeaders toRedmineHeaderForTicketCreate(TicketDto ticketDto) {
		UserEntity entity = userService.getLoggedInUser();
		String clientId = ObjectUtils.isEmpty(entity) ? ticketDto.getMobileNumber(): redmineRegisteredUser;
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Client-Id", clientId);
		headers.add("X-Client-Type", this.getClientType().type);
		log.info("client id:{}, client type:{}", headers.get("X-Client-Id"),headers.get("X-Client-Type"));
		return headers;
	}
	
	public HttpHeaders toRedmineHeader(TicketDto ticketDto) {
		UserEntity entity = userService.getLoggedInUser();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Client-Id", ObjectUtils.isEmpty(entity) ? redmineRegisteredUser: entity.getUserName());
		headers.add("X-Client-Type", ObjectUtils.isEmpty(entity) ? UserTypeEnum.PUBLIC.type :  UserTypeEnum.CUSTOMER_CARE.type);
		log.info("client id:{}, client type:{}", headers.get("X-Client-Id"),headers.get("X-Client-Type"));
		return headers;
	}
	
	public HttpHeaders toRedmineHeaderForPagination(TicketDto ticketDto) {
		UserEntity entity = userService.getLoggedInUser();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Client-Id", entity.getUserName());
		headers.add("X-Client-Type", this.getClientType().type);
		log.info("client id:{}, client type:{}", headers.get("X-Client-Id"),headers.get("X-Client-Type"));
		return headers;
	}
	
	public UserTypeEnum getClientType() {
		UserEntity entity = userService.getLoggedInUser();
		if(ObjectUtils.isNotEmpty(entity)) {
			return UserTypeEnum.CUSTOMER_CARE;
		}
		return UserTypeEnum.PUBLIC;
	}

	public Map<String, String> toPaginationDto(PaginationRequestDto ulrd) {
		UserEntity entity = userService.getLoggedInUser();
		String loggedInUser = ObjectUtils.isNotEmpty(entity) ? entity.getUserName() : null;
		Map<String, String> params = new HashedMap<String, String>();
		params.put("page", String.valueOf(ulrd.getPage().getCurrent() - 1));
		params.put("size", String.valueOf(ulrd.getPage().getSize()));
		params.put("loggedInUser", loggedInUser);
		if (CollectionUtils.isNotEmpty(ulrd.getFilters())) {
			for (FilterDto filter : ulrd.getFilters()) {
				log.info("key: {}, value:{}", filter.getProperty(), filter.getValue());
				if(ObjectUtils.isNotEmpty(filter.getValue())) {
					if(filter.getProperty().equals("startDate") || filter.getProperty().equals("endDate")) {
						LocalDate startDate = dateUtil.toLocalDate(filter.getValue(), DateUtil._fronendFormat);
						params.put(filter.getProperty(), startDate.toString());
					} else {
						params.put(filter.getProperty(), filter.getValue());
					}
				}
			}
		}
		params.put("isMyDashboard", StringUtils.isEmpty(params.get("dashboard")) ? "false" : params.get("dashboard").equals("mine") ? "true": "false");
		return params;
	}
}
