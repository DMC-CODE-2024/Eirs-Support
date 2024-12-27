package com.gl.ceir.panel.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserFeatureDto;
import com.gl.ceir.panel.dto.response.UserFeaturePaginationDto;
import com.gl.ceir.panel.dto.response.UserFeatureViewDto;
import com.gl.ceir.panel.entity.app.FeatureEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserFeatureEntity;
import com.gl.ceir.panel.entity.app.UserFeatureId;
import com.gl.ceir.panel.repository.app.FeatureRepository;
import com.gl.ceir.panel.repository.app.UserFeatureRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.UserFeatureCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class UserFeatureService {
	private final UserFeatureRepository userFeatureRepository;
	private final UserFeatureCriteriaService userFeatureCriteriaService;
	private final UserRepository userRepository;
	private final FeatureRepository featureRepository;
	private final AuditTrailService auditTrailService;
	
	public UserEntity save(UserFeatureDto userFeatureDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity loggedEntity = userRepository.findByUserName(user.getUsername()).get();
		
		Optional<UserEntity> oue= userRepository.findById(userFeatureDto.getUserId());
		UserEntity userEntity = oue.get();
		Set<FeatureEntity> features = new HashSet<>();
		userFeatureDto.getFeatures().forEach(roleId -> {
			Optional<FeatureEntity> role= featureRepository.findById(roleId);
			features.add(role.get());
		});
		List<UserFeatureEntity> userGroups = userFeatureDto.getFeatures().stream()
				.map(g -> UserFeatureEntity.builder().status(StatusEnum.ACTIVE.status)
						.id(UserFeatureId.builder().userId(userFeatureDto.getUserId()).featureId(g).build())
						.createdBy(loggedEntity.getId()).modifiedBy(loggedEntity.getId()).build())
				.collect(Collectors.toList());
		Set<UserFeatureId> ids = userGroups.stream().map(g -> g.getId()).collect(Collectors.toSet());
		List<UserFeatureEntity> list = userFeatureRepository.findByIdUserId(userFeatureDto.getUserId());
		List<UserFeatureEntity> gfIds = list.stream().filter(f -> ids.contains(f.getId()) == false)
				.map(f -> f.toBuilder().status(StatusEnum.INACTIVE.status).build()).collect(Collectors.toList());
		userFeatureRepository.saveAll(gfIds);
		userFeatureRepository.saveAll(userGroups);
		FeatureEnum feature = FeatureEnum.UserFeature;
		ActionEnum action = ObjectUtils.isEmpty(userFeatureDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature, user.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return userEntity;
	}
	
	public UserEntity update(UserFeatureDto userGroupDto, Long id, HttpServletRequest request) {
		return this.save(userGroupDto.toBuilder().id(id).build(),request);
	}
	public Page<?> pagination(PaginationRequestDto ulrd) {
		return userFeatureCriteriaService.pagination(ulrd);
	}
	public boolean delete(List<UserFeatureId> ids,HttpServletRequest request) {
		List<UserFeatureEntity> list = userFeatureRepository.findByIdIn(ids);
		userFeatureRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		FeatureEnum feature = FeatureEnum.UserFeature;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature,
				ids.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return true;
	}
	public boolean active(List<UserFeatureId> ids) {
		List<UserFeatureEntity> list = userFeatureRepository.findByIdIn(ids);
		userFeatureRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}
	public UserFeaturePaginationDto viewById(Long id) {
		Optional<UserEntity> uoptional = userRepository.findById(id);
		UserFeaturePaginationDto ufpd = UserFeaturePaginationDto.builder().build();
		BeanUtils.copyProperties(uoptional.isPresent() ? uoptional.get(): UserFeatureEntity.builder().build(), ufpd);
		return ufpd;
	}
	public List<UserFeatureViewDto> getById(Long userId) {
		List<UserFeatureEntity> list = userFeatureRepository.findByIdUserIdAndStatus(userId, StatusEnum.ACTIVE.status);
		List<UserFeatureViewDto> rlist = new ArrayList<UserFeatureViewDto>();
		list.forEach(l -> {
			UserFeatureViewDto ufvd = UserFeatureViewDto.builder().build();
			BeanUtils.copyProperties(l, ufvd);
			rlist.add(ufvd);
		});
		return rlist;
	}
}
