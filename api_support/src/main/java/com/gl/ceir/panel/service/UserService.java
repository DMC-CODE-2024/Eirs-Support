package com.gl.ceir.panel.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.HttpStatusEnum;
import com.gl.ceir.panel.constant.LogicalDirectoryEnum;
import com.gl.ceir.panel.constant.MessaeEnum;
import com.gl.ceir.panel.constant.OtpChannelTypeEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.constant.UserTypeEnum;
import com.gl.ceir.panel.dto.ChangePasswordDto;
import com.gl.ceir.panel.dto.NotificationDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.dto.UserGroupDto;
import com.gl.ceir.panel.dto.UserUpdateDto;
import com.gl.ceir.panel.dto.request.LoginRequest;
import com.gl.ceir.panel.dto.response.ApiStatusDto;
import com.gl.ceir.panel.dto.response.AuthDto;
import com.gl.ceir.panel.dto.response.BooleanDto;
import com.gl.ceir.panel.dto.response.FeautreMenuDto;
import com.gl.ceir.panel.dto.response.MessageResponse;
import com.gl.ceir.panel.entity.app.FeatureEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserGroupEntity;
import com.gl.ceir.panel.entity.app.UserProfileEntity;
import com.gl.ceir.panel.entity.app.UserSecurityQuestionEntity;
import com.gl.ceir.panel.entity.app.UserSecurityQuestionId;
import com.gl.ceir.panel.repository.app.FeatureRepository;
import com.gl.ceir.panel.repository.app.UserGroupRepository;
import com.gl.ceir.panel.repository.app.UserProfileRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.repository.remote.NotificationRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.UserCriteriaService;
import com.gl.ceir.panel.service.helper.NotificationHelper;
import com.gl.ceir.panel.util.FileUploadUtil;
import com.gl.ceir.panel.util.OperatorUtil;
import com.gl.ceir.panel.util.OtpUtil;
import com.gl.ceir.panel.util.PlaceholderUtil;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor()
@Log4j2
public class UserService {
	@Value("${eirs.panel.source.path:}")
	private String basepath;
	private static UserService instance;
	private final UserRepository userRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserCriteriaService userCriteriaService;
	private final PasswordEncoder encoder;
	private final FileUploadUtil fileUploadUtil;
	private final UserGroupService userGroupService;
	private final AuthenticationManager authenticationManager;
	private final UserPermissionService permissionService;
	private final NotificationRepository notificationRepository;
	private final NotificationHelper notificationHelper;
	private final UserGroupRepository userGroupRepository;
	private final AuditTrailService auditTrailService;
	private final UserPasswordService passwordService;
	private final OtpUtil otpUtil;
	private final PassiveExpiringMap<String, String> otpMap;
	private final FeatureRepository featureRepository;
	private final OperatorUtil operatorUtil;
	private final FeatureService featureService;
	private final RedmineBackendService redmineBackendService;
	private final PlaceholderUtil placeholderUtil; 
	@Value("${eirs.otp.mobile.change.message:Please use this otp: {otp} to access the tickets}")
	private String mobileChangeMessage;
	@Value("${eirs.otp.email.change.message:Please use this otp: {otp} to access the tickets}")
	private String emailChangeMessage;
	@Value("${eirs.panel.password.validity:30}")
	private int passwordValidity;
	@Value("${eirs.panel.user.expiration.prompt.days:5}")
	private int userExpirationPromptDays;

	@Value("${eirs.register.user.message:Please use this username: %s to login the eirs portal}")
	private String registerUserMessage;
	@Value("${register.user.subject:Reset Password}")
	private String registerUserSubject;
	@Value("${eirs.system.admin.support.group:}")
	private String systemAdminSupportGroup;
	@Value("${eirs.customer.care.group:}")
	private String customerCareGroup;
	
	@Value("${eirs.user.create.email:Please login with username:[%] and temporary password:[%s]}")
	private String userCreateEmail;
	@Value("${eirs.user.create.email.subject:Credentials to access}")
	private String userCreateEmailSubject;
	
	@Value("${eirs.reset.password.message:Password reset, please login with temporary password:[%s]}")
	private String resetPasswordMessage;
	@Value("${eirs.reset.password.subject:Reset Password}")
	private String resetPasswordSubject;
	
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
	
	@Value("${eirs.country.code:+855}")
	private String countryCode;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static UserService getUserService() {
		return instance;
	}

	public List<UserEntity> getUsers() {
		return userRepository.findByCreatedByInAndCurrentStatus(permissionService.permissions().getUserIds(),
				StatusEnum.ACTIVE.status);
	}

	public UserEntity getLoggedInUser() {
		UserEntity entity = null;
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			entity = userRepository.findByUserName(user.getUsername()).get();
		} catch (Exception e) {
			//log.info("Error while user regiestered by public api: {}", e.getMessage());
		}
		return entity;
	}

	public ResponseEntity<?> prevalidate() {
		UserEntity entity = getLoggedInUser();
		AuthDto authDto = AuthDto.builder().isLogin(ObjectUtils.isNotEmpty(entity))
				.isPasswordExpire(
						ObjectUtils.isNotEmpty(entity) && entity.getPasswordDate().isBefore(LocalDateTime.now()))
				.isTemparoryPassword(false)
				.id(ObjectUtils.isNotEmpty(entity) ? entity.getId() : null)
				.userName(ObjectUtils.isNotEmpty(entity) ? entity.getUserName() : null).build();
		log.info("User allow login:{}", authDto);
		return new ResponseEntity<>(authDto, HttpStatus.OK);
	}

	public ResponseEntity<?> save(UserCreateDto ucd, HttpServletRequest request) {
		boolean isexisting = false;
		String password = ucd.getPassword();
		try {
			UserEntity userEntity = null;
			log.info("User detail:{}", ucd);
			ucd.setUserName(ObjectUtils.isNotEmpty(ucd.getUserName()) ? ucd.getUserName() : ucd.getEmail());

			try {
				UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				userEntity = userRepository.findByUserName(user.getUsername()).get();
			} catch (Exception e) {
				log.info("Error while user regiestered by public api: {}", e.getMessage());
			}

			if (ObjectUtils.isNotEmpty(ucd.getPassword()))ucd.setPassword(encoder.encode(ucd.getPassword()));
			String nidlogicalpath = fileUploadUtil.upload(ucd.getNidFile(), basepath, LogicalDirectoryEnum.user.name());
			String idcardlogicalpath = fileUploadUtil.upload(ucd.getIdCardFile(), basepath, LogicalDirectoryEnum.user.name());
			String userlogicalpath = fileUploadUtil.upload(ucd.getPhotoFile(), basepath, LogicalDirectoryEnum.user.name());

			log.info("nid:{},id:{},photo:{}", nidlogicalpath, idcardlogicalpath, userlogicalpath);

			log.info("User detail:{}", ucd);
			UserProfileEntity upe = UserProfileEntity.builder().build();
			BeanUtils.copyProperties(ucd, upe);
			log.info("profile:{}", upe);

			if (ObjectUtils.isNotEmpty(nidlogicalpath))
				upe.setNidFileName(nidlogicalpath);
			if (ObjectUtils.isNotEmpty(idcardlogicalpath))
				upe.setIdCardFileName(idcardlogicalpath);
			if (ObjectUtils.isNotEmpty(userlogicalpath))
				upe.setPhotoFileName(userlogicalpath);

			UserEntity tobesave = UserEntity.builder().build();
			BeanUtils.copyProperties(ucd, tobesave);
			
			if (ObjectUtils.isNotEmpty(ucd.getId())) {
				isexisting = true;
				UserEntity existing = userRepository.findById(ucd.getId()).get();
				upe = upe.toBuilder().id(existing.getProfile().getId()).phoneNo(existing.getProfile().getPhoneNo())
						.email(existing.getProfile().getEmail()).user(userEntity).build();
				tobesave = tobesave.toBuilder().password(existing.getPassword()).userName(existing.getUserName())
						.passwordDate(existing.getPasswordDate()).roles(existing.getRoles())
						.approvedBy(existing.getApprovedBy()).approvedDate(existing.getApprovedDate())
						.createdBy(existing.getCreatedBy()).currentStatus(existing.getCurrentStatus())
						.failedAttempt(existing.getFailedAttempt()).lastLoginDate(existing.getLastLoginDate())
						.modifiedBy(existing.getModifiedBy()).previousStatus(existing.getPreviousStatus())
						.referenceId(existing.getReferenceId()).remark(existing.getRemark())
						.userLanguage(existing.getUserLanguage()).parent(existing.getParent())
						.currentStatus(existing.getCurrentStatus()).build();
			} else {
				log.info("User to save:{}", tobesave);
				boolean emailalreadyExist = this.emailExist(ucd.getEmail());
				log.info("Email already exsit:{} in system:{}", emailalreadyExist, ucd.getEmail());
				if(emailalreadyExist) {
					return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.FAILED.status)
							.code(HttpStatus.OK).message(MessaeEnum.EMAIL_ALREADY_EXIST.message).build());
				}
				tobesave = tobesave.toBuilder().currentStatus(StatusEnum.INACTIVE.status)
						.passwordDate(LocalDateTime.now().plusDays(passwordValidity)).userName(RandomStringUtils.randomAlphabetic(8))
						.createdBy(userEntity.getId()).modifiedBy(String.valueOf(userEntity.getId())).build();
			}

			tobesave = userRepository.save(tobesave.toBuilder().profile(upe).build());
			userProfileRepository.updateUserId(tobesave.getProfile().getId(), tobesave.getId());
			

			List<UserSecurityQuestionEntity> questions = new ArrayList<>();
			if (ObjectUtils.isNotEmpty(ucd.getQuestion1())) {
				questions.add(UserSecurityQuestionEntity.builder().answer(ucd.getAnswer1()).id(UserSecurityQuestionId
						.builder().questionId(ucd.getQuestion1()).userId(tobesave.getId()).build()).build());
			}
			if (ObjectUtils.isNotEmpty(ucd.getQuestion2())) {
				questions.add(UserSecurityQuestionEntity.builder().answer(ucd.getAnswer2()).id(UserSecurityQuestionId
						.builder().questionId(ucd.getQuestion2()).userId(tobesave.getId()).build()).build());
			}
			if (ObjectUtils.isNotEmpty(ucd.getQuestion3())) {
				questions.add(UserSecurityQuestionEntity.builder().answer(ucd.getAnswer3()).id(UserSecurityQuestionId
						.builder().questionId(ucd.getQuestion3()).userId(tobesave.getId()).build()).build());
			}

			UserEntity savedEntity = userRepository.findByUserName(tobesave.getUserName()).get();
			if (ObjectUtils.isEmpty(userEntity)) userEntity = savedEntity;
			userRepository.save(savedEntity.toBuilder().createdBy(userEntity.getId()).modifiedBy(String.valueOf(userEntity.getId())).build());

			List<UserGroupEntity> groups = userGroupRepository.findByIdUserId(savedEntity.getId());
			if (CollectionUtils.isNotEmpty(groups)) {
				UserGroupDto ugd = UserGroupDto.builder().userId(savedEntity.getId())
						.groups(groups.stream().map(g -> g.getId().getGroupId()).collect(Collectors.toList())).build();
				log.info("User group service:{}", ugd);
				userGroupService.save(ugd,request);
			}
			if (isexisting == false) {
				try {
					FeatureEntity feature = featureRepository.findOneByLink(FeatureEnum.User.getName());
					if(ObjectUtils.isNotEmpty(feature)) {
						Map<String, String> values = new HashMap<String, String>();
						values.put("password", password);
						values.put("email", ObjectUtils.isNotEmpty(tobesave.getProfile()) ? tobesave.getProfile().getEmail(): "");
						values.put("firstName", ObjectUtils.isNotEmpty(tobesave.getProfile()) ? tobesave.getProfile().getFirstName(): "");
						values.put("lastName", ObjectUtils.isNotEmpty(tobesave.getProfile()) ? tobesave.getProfile().getLastName(): "");
						values.put("mobile", ObjectUtils.isNotEmpty(tobesave.getProfile()) ? tobesave.getProfile().getPhoneNo(): "");
						values.put("username", tobesave.getUserName());
						NotificationDto notification = NotificationDto.builder()
								.channelType(emailPromoChannelType)
								.message(placeholderUtil.message(values, userCreateEmail))
								.subject(placeholderUtil.message(values, userCreateEmailSubject)).featureName(feature.getFeatureName())
								.featureTxnId(String.valueOf(System.currentTimeMillis())).userId(ObjectUtils.isNotEmpty(savedEntity) ? savedEntity.getId(): null)
								.featureId(feature.getId()).email(savedEntity.getProfile().getEmail()).build();
						log.info("Temparory password and user id in email:{}", notification);
						notificationHelper.send(notification);
					}
					
				} catch (Exception e) {
					log.error("Error while send notification:{}", e.getMessage());
				}
			}
			redmineBackendService.saveUser(savedEntity.getUserName());
			FeatureEnum feature = FeatureEnum.User;
			ActionEnum action = isexisting ? ActionEnum.Add: ActionEnum.Update;
			String details = String.format("%s's User:%s has been:%s with userid:%s and temparory password:%s", feature,
					savedEntity.getId(), action.getName(), savedEntity.getUserName(), password);
			auditTrailService.audit(request, feature, action, details);
			return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.SUCCESS.status)
					.code(HttpStatus.OK).message(MessaeEnum.SAVE_USER_SUCCESS.message).build());
		} catch (Exception e) {
			log.info("Message: {}", e.getMessage());
			return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.FAILED.status)
					.code(HttpStatus.OK).message(MessaeEnum.SAVE_USER_FAILED.message).build());
		}
	}

	public ResponseEntity<?> update(UserUpdateDto userDto, Long id, HttpServletRequest request) {
		UserEntity group = this.viewById(id);
		UserCreateDto ucreate = UserCreateDto.builder().build();
		BeanUtils.copyProperties(userDto, ucreate);
		return this.save(ucreate.toBuilder().id(group.getId()).build(),request);
	}

	public ResponseEntity<?> changePassword(ChangePasswordDto cpd, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userEntity.getUserName(), cpd.getOldPassword()));
		} catch (Exception e) {
			log.info("User:{} Old Password not matched", user.getUsername());
			return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.FAILED.status)
					.code(HttpStatus.OK).message(MessaeEnum.OLD_PASS_NOT_MATCHED.message).build());
		}
		if (ObjectUtils.isNotEmpty(cpd.getNewPassword())) {
			userEntity.setPassword(encoder.encode(cpd.getNewPassword()));
			userEntity.setPasswordDate(LocalDateTime.now().plusDays(passwordValidity));
		}
		if(passwordService.isMatchedWithHistoryPassword(userEntity,cpd)==false) {
			passwordService.save(userRepository.save(userEntity));
			log.info("Password changed successfully");
			FeatureEnum feature = FeatureEnum.User;
			ActionEnum action = ActionEnum.PasswordChanged;
			String details = String.format("%s [%s] is %s", feature,userEntity.getId(), action.getName());
			auditTrailService.audit(request, feature, action, details);
			return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.SUCCESS.status)
					.code(HttpStatus.OK).message(MessaeEnum.PASS_CHANGE_SUCCESS.message).build());
		} else {
			return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.FAILED.status)
					.code(HttpStatus.OK).message(MessaeEnum.PASS_HISTORY_MATCHED.message).build());
		}
		
	}

	public UserEntity viewById(Long id) {
		Optional<UserEntity> uoptional = userRepository.findById(id);
		return uoptional.isPresent() ? uoptional.get() : null;
	}

	public boolean emailExist(String email) {
		UserProfileEntity profile = userProfileRepository.findByEmail(email);
		log.info("Email:{} with profile:{}", email, ObjectUtils.isNotEmpty(profile));
		return ObjectUtils.isNotEmpty(profile);
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return userCriteriaService.pagination(ulrd);
	}

	public BooleanDto isAlertForPasswordExpire() {
		BooleanDto dto = BooleanDto.builder().build();
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
			boolean allow = ObjectUtils.isNotEmpty(userEntity.getPasswordDate())
					&& userEntity.getPasswordDate().isBefore(LocalDateTime.now().plusDays(userExpirationPromptDays));
			log.info("password expiration date:{},allow:{}", userEntity.getPasswordDate(), allow);
			long days = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now().plusDays(1),
					userEntity.getPasswordDate().toLocalDate());
			days = days < 0 ? 0: days;
			dto = dto.toBuilder().allow(allow).days(days).build();
			log.info("Password expire response:{}", dto);
			return dto;
		} catch (Exception e) {
			log.info("Error while user regiestered by public api: {}", e.getMessage());
		}
		return dto;
	}

	public boolean delete(List<Long> ids, HttpServletRequest request) {
		List<UserEntity> list = userRepository.findByIdIn(ids);
		userRepository.saveAll(list.stream().map(l -> l.toBuilder().currentStatus(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		
		FeatureEnum feature = FeatureEnum.User;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature,
				ids.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
		auditTrailService.audit(request, feature, action, details);
		
		return true;
	}

	public boolean active(List<Long> ids, HttpServletRequest request) {
		List<UserEntity> list = userRepository.findByIdIn(ids);
		userRepository.saveAll(list.stream().map(l -> l.toBuilder().currentStatus(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		FeatureEnum feature = FeatureEnum.User;
		ActionEnum action = ActionEnum.Active;
		String details = String.format("%s [%s] is %s", feature,
				ids.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return true;
	}

	public ResponseEntity<?> resetpassword(List<Long> ids, HttpServletRequest request) {
		List<UserEntity> users = userRepository.findByIdIn(ids);
		FeatureEntity featureobj = featureRepository.findOneByLink(FeatureEnum.User.getName());
		UserEntity entity = getLoggedInUser();
		users.forEach(user -> {
			try {
				if(ObjectUtils.isNotEmpty(featureobj)) {
					String password = RandomStringUtils.randomAlphabetic(8);
					user.setPasswordDate(LocalDateTime.now());
					user.setPassword(encoder.encode(password));
					Map<String, String> values = new HashMap<String, String>();
					values.put("password", password);
					values.put("email", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getEmail(): "");
					values.put("firstName", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getFirstName(): "");
					values.put("lastName", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getLastName(): "");
					values.put("mobile", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getPhoneNo(): "");
					values.put("username", user.getUserName());
					
					NotificationDto notification = NotificationDto.builder()
							.channelType(OtpChannelTypeEnum.EMAIL.name())
							.message(placeholderUtil.message(values, resetPasswordMessage))
							.subject(placeholderUtil.message(values, resetPasswordSubject))
							.email(user.getProfile().getEmail()).featureName(featureobj.getFeatureName())
							.featureTxnId(String.valueOf(System.currentTimeMillis())).featureId(featureobj.getId())
							.userId(ObjectUtils.isNotEmpty(entity) ? entity.getId(): null)
							.build();
					notificationHelper.send(notification);
					FeatureEnum feature = FeatureEnum.User;
					ActionEnum action = ActionEnum.ResetPassword;
					String details = String.format("%s's password [%s] has been %s", feature,
							ids.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
					auditTrailService.audit(request, feature, action, details);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		userRepository.saveAll(users);
		return ResponseEntity.ok().body(
				MessageResponse.builder().message("saveUserSuccess").status("success").code(HttpStatus.OK).build());
	}

	public UserTypeEnum findUserType() {
		Set<FeautreMenuDto> menu= featureService.menu();
		List<String> cares = new ArrayList<String>(Arrays.asList("ticket","register-ticket","check-ticket-status","dashboard"));
		List<String> support = new ArrayList<String>(Arrays.asList("ticket"));
		
		List<String> caremenu = menu.stream().filter(m -> cares.contains(m.getLink())).map(m -> m.getLink()).collect(Collectors.toList());
		List<String> supportmenu = menu.stream().filter(m -> support.contains(m.getLink())).map(m -> m.getLink()).collect(Collectors.toList());
		
		UserTypeEnum userType = UserTypeEnum.PUBLIC;
		UserEntity entity = this.getLoggedInUser();
		if (ObjectUtils.isNotEmpty(entity)) {
			if(ObjectUtils.isNotEmpty(caremenu) && caremenu.size() == cares.size() && menu.size() <= 5) {
				return UserTypeEnum.CARE;
			} else if(ObjectUtils.isNotEmpty(support) && supportmenu.size() == support.size() && menu.size() <= 2) {
				return UserTypeEnum.SUPPORT;
			} else {
				return UserTypeEnum.ADMIN;
			}
		}
		return userType;
	}

	public ResponseEntity<?> users(String userName) {
		Optional <UserEntity> user = userRepository.findByUserName(userName);
		Set<Object> users = new HashSet<>();
		if(user.isPresent()) {
			users.addAll(permissionService.findParentChildUser(user.get()));
			log.info("Found valid users for ticket:{}", users);
		} else {
			users.add(loggedOutUserType);
		}
		log.info("Users list: {}", users);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	public ResponseEntity<?> gropus(Long groupId) {
		List<Long> groups = new ArrayList<>();
		return new ResponseEntity<>(permissionService.groups(groups, groupId), HttpStatus.OK);
	}
	public ApiStatusDto sendotp(OtpChannelTypeEnum otpchannel, String emailormsisdn) {
		try {
			UserEntity user = getLoggedInUser();
			FeatureEntity featureobj = featureRepository.findOneByLink(FeatureEnum.User.getName());
			String otp = otpUtil.phoneOtp(emailormsisdn);
			log.info("Generated otp:{}, for:{},senderid:{}", otp, otpchannel, emailormsisdn);
			Map<String, String> values = new HashMap<String, String>();
			values.put("otp", otp);
			values.put("email", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getEmail(): "");
			values.put("firstName", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getFirstName(): "");
			values.put("lastName", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getLastName(): "");
			values.put("mobile", ObjectUtils.isNotEmpty(user.getProfile()) ? user.getProfile().getPhoneNo(): "");
			values.put("username", user.getUserName());
			if(otpchannel==OtpChannelTypeEnum.EMAIL) {
				notificationHelper.send(NotificationDto.builder().channelType(emailChannelType).email(emailormsisdn)
						.message(placeholderUtil.message(values, emailChangeMessage)).featureName(featureobj.getFeatureName())
						.featureId(featureobj.getId()).featureTxnId(String.valueOf(System.currentTimeMillis()))
						.userId(ObjectUtils.isNotEmpty(user) ? user.getId(): null).build());
				
			} else if(otpchannel==OtpChannelTypeEnum.SMS) {
				String countrycode = countryCode.startsWith("+") ? countryCode.substring(1, countryCode.length()): countryCode; 
				emailormsisdn = emailormsisdn.startsWith(countrycode) ? emailormsisdn : countrycode + emailormsisdn;
				notificationHelper.send(NotificationDto.builder().channelType(smsChannelType)
						.msisdn(emailormsisdn).operatorName(operatorUtil.getOperator(emailormsisdn))
						.message(placeholderUtil.message(values, mobileChangeMessage)).featureName(featureobj.getFeatureName())
						.featureId(featureobj.getId()).featureTxnId(String.valueOf(System.currentTimeMillis()))
						.userId(ObjectUtils.isNotEmpty(user) ? user.getId(): null).build());
			}
			otpMap.put(emailormsisdn, otp);
			return ApiStatusDto.builder().message("sendOtpSuccess").build();
		}catch(Exception e) {
			e.printStackTrace();
			return ApiStatusDto.builder().message("sendOtpFailed").build();
		}
	}
	public ApiStatusDto verifyOtp(String emailormsisdn, String otp) {
		String sentotp = otpMap.get(emailormsisdn);
		return ObjectUtils.isNotEmpty(sentotp) && otp.equals(sentotp)
				? ApiStatusDto.builder().message("verifyOtpSuccess").build()
				: ApiStatusDto.builder().message("verifyOtpFailed").build();
	}
	public ResponseEntity<?> updateEmailAndMsisdn(Long id, String email, String msisdn) {
		Optional<UserEntity> oentity = userRepository.findById(id);
		if(oentity.isPresent()) {
			UserEntity entity = oentity.get();
			entity.getProfile().setPhoneNo(msisdn);
			entity.getProfile().setEmail(email);
			userRepository.save(entity);
			redmineBackendService.saveUser(entity.getUserName());
			return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.SUCCESS.status)
					.code(HttpStatus.OK).message(MessaeEnum.SAVE_USER_SUCCESS.message).build());
		}
		return ResponseEntity.ok().body(MessageResponse.builder().status(HttpStatusEnum.FAILED.status)
				.code(HttpStatus.OK).message(MessaeEnum.SAVE_USER_FAILED.message).build());
	}
	public UserEntity updateFailedAttempt(LoginRequest loginRequest) {
		try {
			Optional<UserEntity> entity = userRepository.findByUserName(loginRequest.getUserName());
			if(entity.isPresent()) {
				return userRepository.save(entity.get().toBuilder().failedAttempt(entity.get().getFailedAttempt() + 1).build());
			}
		}catch(Exception e) {
			log.error("Error while update failed attempt:{}", e.getMessage());
		}
		return null;
	}
}
