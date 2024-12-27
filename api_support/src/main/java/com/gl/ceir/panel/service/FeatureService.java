package com.gl.ceir.panel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.LogicalDirectoryEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.FeatureDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.response.FeautreMenuDto;
import com.gl.ceir.panel.entity.app.FeatureEntity;
import com.gl.ceir.panel.entity.app.FeatureModuleEntity;
import com.gl.ceir.panel.entity.app.FeatureModuleId;
import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupFeatureEntity;
import com.gl.ceir.panel.entity.app.GroupRoleEntity;
import com.gl.ceir.panel.entity.app.ModuleEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.FeatureModuleRepository;
import com.gl.ceir.panel.repository.app.FeatureRepository;
import com.gl.ceir.panel.repository.app.GroupFeatureRepository;
import com.gl.ceir.panel.repository.app.GroupRoleRepository;
import com.gl.ceir.panel.repository.app.ModuleRepository;
import com.gl.ceir.panel.repository.app.TagRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.FeatureCriteriaService;
import com.gl.ceir.panel.service.helper.FeatureMenuHelper;
import com.gl.ceir.panel.util.DirectoryUtil;
import com.gl.ceir.panel.util.FileUploadUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class FeatureService {	
	@Value("${eirs.panel.source.path:}")
	private String basepath;
	private final FeatureRepository featureRepository;
	private final ModuleRepository moduleRepository;
	private final FeatureCriteriaService featureCriteriaService;
	private final UserRepository userRepository;
	private final TagRepository tagRepository;
	private final UserPermissionService permissionService;
	private final FeatureMenuHelper featureMenuHelper;
	private final DirectoryUtil directoryUtil;
	private final FileUploadUtil fileUploadUtil;
	private final FeatureModuleService featureModuleService;
	private final FeatureModuleRepository featureModuleRepository;
	private final AuditTrailService auditTrailService;
	private final GroupFeatureRepository groupFeatureRepository;
	private final GroupRoleRepository groupRoleRepository;

	public FeatureEntity save(FeatureDto featureDto, HttpServletRequest request) {
		FeatureEntity featureEntity = null;
		FeatureEntity existing = null;
		try {
			List<FeatureModuleEntity> list = featureModuleRepository.findByIdFeatureId(featureDto.getId());
			if (ObjectUtils.isNotEmpty(featureDto.getId()))
				existing = this.getById(featureDto.getId());
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
			String logicalpath = fileUploadUtil.upload(featureDto.getFile(), basepath,
					LogicalDirectoryEnum.feature.name());
			log.info("Logical path: {}", logicalpath);
			featureEntity = FeatureEntity.builder().id(featureDto.getId()).featureName(featureDto.getFeatureName())
					.description(featureDto.getDescription())
					.category(featureDto.getCategory())
					.logo(ObjectUtils.isEmpty(logicalpath) && ObjectUtils.isNotEmpty(existing) ? existing.getLogo()
							: logicalpath)
					.createdBy(userEntity.getId()).modifiedBy(userEntity.getId()).link(featureDto.getLink()).build();
			featureEntity = featureEntity.toBuilder()
					.status(ObjectUtils.isNotEmpty(existing) && ObjectUtils.isNotEmpty(existing.getStatus())
							? existing.getStatus()
							: StatusEnum.INACTIVE.status)
					.build();
			/*
			if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(featureDto.getModuleTagId())) {
				Optional<ModuleTagEntity> otag = tagRepository.findById(featureDto.getModuleTagId());
				if (otag.isPresent())
					featureEntity = featureEntity.toBuilder().moduleTag(otag.get()).build();
			}*/
			featureEntity = featureRepository.save(featureEntity.toBuilder().modules(list.stream()
					.map(l -> ModuleEntity.builder().id(l.getId().getModuleId()).build()).collect(Collectors.toList()))
					.build());
			list = list.stream().map(l -> FeatureModuleEntity.builder().createdBy(l.getCreatedBy()).modifiedBy(userEntity.getId()).status(l.getStatus()).id(
					FeatureModuleId.builder().featureId(featureDto.getId()).moduleId(l.getId().getModuleId()).build())
					.createdOn(l.getCreatedOn()).updatedOn(l.getUpdatedOn()).build()).collect(Collectors.toList());
			
			FeatureEnum feature = FeatureEnum.Feature;
			ActionEnum action = ObjectUtils.isEmpty(featureDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
			String details = String.format("%s [%s] is %s", feature, featureEntity.getId(), action.getName());
			auditTrailService.audit(request, feature, action, details);
			
			featureModuleRepository.saveAll(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return featureEntity;
	}

	public Set<FeatureEntity> getFeatures() {
		List<GroupEntity> groups = permissionService.getUserGroups();
		List<GroupFeatureEntity> gfeatures = groupFeatureRepository.findByIdGroupIdInAndStatusOrderByDisplayOrder(
				groups.stream().map(m -> m.getId()).collect(Collectors.toList()), StatusEnum.ACTIVE.status);
		List<FeatureEntity> fcreated = new ArrayList<FeatureEntity>();
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserEntity entity = userRepository.findByUserName(user.getUsername()).get();
			fcreated = featureRepository.findByCreatedByAndStatus(entity.getId(), StatusEnum.ACTIVE.status);
		} catch (Exception e) {
			
		}
		Set<FeatureEntity> gf = gfeatures.stream().filter(f -> f.getFeature().getStatus().equals(StatusEnum.ACTIVE.status))
				.map(f -> f.getFeature()).collect(Collectors.toSet());
		gf.addAll(fcreated);
		return gf;
	}
	public List<FeatureEntity> getAllFeaturesByRoleId(Long roleId) {
		List<GroupRoleEntity> groles = groupRoleRepository.findByIdRoleIdAndStatus(roleId, StatusEnum.ACTIVE.status);
		List<Long> gIds = groles.stream().map(gr -> gr.getId().getGroupId()).collect(Collectors.toList());
		List<GroupFeatureEntity> gfeatures = groupFeatureRepository.findByIdGroupIdInAndStatusOrderByDisplayOrder(gIds, StatusEnum.ACTIVE.status);
		return gfeatures.stream().filter(f -> f.getFeature().getStatus().equals(StatusEnum.ACTIVE.status))
				.map(f -> f.getFeature()).collect(Collectors.toList());
	}
	public List<FeatureEntity> getAllFeatures() {
		return featureRepository.findAll();
	}

	public FeatureEntity getById(Long id) {
		Optional<FeatureEntity> optional = featureRepository.findById(id);
		return optional.isPresent() ? optional.get() : FeatureEntity.builder().build();
	}

	public FeatureEntity update(FeatureDto featureDto, Long id, HttpServletRequest request) {
		return this.save(featureDto.toBuilder().id(id).build(),request);
	}

	public FeatureEntity deleteById(Long id) {
		FeatureEntity group = this.getById(id);
		featureRepository.deleteById(id);
		return group;
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return featureCriteriaService.pagination(ulrd);
	}

	public Set<FeautreMenuDto> menu() {
		return featureMenuHelper.menu();
	}

	public boolean delete(List<Long> ids) {
		List<FeatureEntity> list = featureRepository.findByIdIn(ids);
		featureRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public boolean active(List<Long> ids) {
		List<FeatureEntity> list = featureRepository.findByIdIn(ids);
		featureRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}
}
