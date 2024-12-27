package com.gl.ceir.panel.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.FeatureModuleDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.response.FeatureModuleViewDto;
import com.gl.ceir.panel.entity.app.FeatureEntity;
import com.gl.ceir.panel.entity.app.FeatureModuleEntity;
import com.gl.ceir.panel.entity.app.FeatureModuleId;
import com.gl.ceir.panel.entity.app.ModuleEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.FeatureModuleRepository;
import com.gl.ceir.panel.repository.app.FeatureRepository;
import com.gl.ceir.panel.repository.app.ModuleRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.FeatureModuleCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class FeatureModuleService {
	private final FeatureModuleRepository featureModuleRepository;
	private final FeatureModuleCriteriaService featureModuleCriteriaService;
	private final ModuleRepository moduleRepository;
	private final FeatureRepository featureRepository;
	private final UserRepository userRepository;
	private final UserPermissionService permissionService;
	private final AuditTrailService auditTrailService;

	public FeatureEntity save(FeatureModuleDto featureModuleDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
		Optional<FeatureEntity> oue = featureRepository.findById(featureModuleDto.getFeatureId());
		FeatureEntity featureEntity = oue.get();
		List<ModuleEntity> modules = new ArrayList<>();
		List<FeatureModuleEntity> userGroups = featureModuleDto.getModules().stream()
				.map(g -> FeatureModuleEntity.builder().status(StatusEnum.ACTIVE.status)
						.id(FeatureModuleId.builder().featureId(featureModuleDto.getFeatureId()).moduleId(g).build())
						.createdBy(userEntity.getId()).modifiedBy(userEntity.getId()).createdOn(LocalDateTime.now())
						.build())
				.collect(Collectors.toList());
		Set<FeatureModuleId> ids = userGroups.stream().map(g -> g.getId()).collect(Collectors.toSet());
		List<FeatureModuleEntity> features = featureModuleRepository.findByIdFeatureId(featureModuleDto.getFeatureId());
		List<FeatureModuleEntity> gfIds = features.stream().filter(f -> ids.contains(f.getId()) == false)
				.map(f -> f.toBuilder().status(StatusEnum.INACTIVE.status).build()).collect(Collectors.toList());
		if (ObjectUtils.isNotEmpty(featureModuleDto.getId())) {
			userGroups = userGroups.stream().map(m -> {
				FeatureModuleEntity fme = features.stream()
						.filter(f -> f.getFeature().getId().equals(featureModuleDto.getFeatureId())
								&& f.getModule().getId().equals(m.getId().getModuleId()))
						.findFirst().orElse(null);
				return m.toBuilder().createdOn(ObjectUtils.isNotEmpty(fme) ? fme.getCreatedOn() : LocalDateTime.now())
						.build();
			}).collect(Collectors.toList());
		}
		featureModuleRepository.saveAll(gfIds);
		featureModuleRepository.saveAll(userGroups);
		
		FeatureEnum feature = FeatureEnum.FeatureModule;
		ActionEnum action = ObjectUtils.isEmpty(featureModuleDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature, featureEntity.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return featureEntity;
	}

	public TreeSet<FeatureModuleEntity> getFeatureModules() {
		List<FeatureEntity> features = featureRepository
				.findByCreatedByInAndStatus(permissionService.permissions().getUserIds(), StatusEnum.ACTIVE.status);
		List<FeatureModuleEntity> list = featureModuleRepository
				.findByIdFeatureIdIn(features.stream().map(f -> f.getId()).collect(Collectors.toList()));
		return list.stream().collect(Collectors.toCollection(() -> new TreeSet<FeatureModuleEntity>(
				(p1, p2) -> p1.getFeature().getId().compareTo(p2.getFeature().getId()))));
	}

	public FeatureEntity update(FeatureModuleDto featureModuleDto, Long id, HttpServletRequest request) {
		return this.save(featureModuleDto.toBuilder().id(id).build(),request);
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return featureModuleCriteriaService.pagination(ulrd);
	}

	public boolean delete(List<FeatureModuleId> ids) {
		List<FeatureModuleEntity> list = featureModuleRepository.findByIdIn(ids);
		featureModuleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public boolean active(List<FeatureModuleId> ids) {
		List<FeatureModuleEntity> list = featureModuleRepository.findByIdIn(ids);
		featureModuleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public List<FeatureModuleEntity> getById(Long featureId) {
		List<FeatureModuleViewDto> rlist = new ArrayList<FeatureModuleViewDto>();
		List<FeatureModuleEntity> list = featureModuleRepository.findByIdFeatureIdAndStatus(featureId,
				StatusEnum.ACTIVE.status);
		return list;
	}
}
