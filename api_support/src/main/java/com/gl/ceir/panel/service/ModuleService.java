package com.gl.ceir.panel.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.ModuleDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.entity.app.ModuleEntity;
import com.gl.ceir.panel.entity.app.ModuleTagEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.ModuleRepository;
import com.gl.ceir.panel.repository.app.TagRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.ModuleCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class ModuleService {
	private final ModuleRepository moduleRepository;
	private final TagRepository tagRepository;
	private final ModuleCriteriaService moduleCriteriaService;
	private final UserRepository userRepository;
	private final AuditTrailService auditTrailService;

	public ModuleEntity save(ModuleDto tagDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
		ModuleEntity module = org.apache.commons.lang3.ObjectUtils.isNotEmpty(tagDto.getId()) ? this.getById(tagDto.getId()): null;
		ModuleEntity me = ModuleEntity.builder().id(tagDto.getId()).moduleName(tagDto.getModuleName())
				.description(tagDto.getDescription()).createdBy(userEntity.getId()).modifiedBy(userEntity.getId())
				.status(org.apache.commons.lang3.ObjectUtils.isNotEmpty(module) ? module.getStatus(): StatusEnum.INACTIVE.status).build();
		if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(tagDto.getModuleTagId())) {
			Optional<ModuleTagEntity> otag = tagRepository.findById(tagDto.getModuleTagId());
			if (otag.isPresent())
				me = me.toBuilder().moduleTag(otag.get()).build();
		}
		module = moduleRepository.save(me);
		FeatureEnum feature = FeatureEnum.Module;
		ActionEnum action = ObjectUtils.isEmpty(tagDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature, module.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		
		return module;
	}

	public List<ModuleEntity> getModules() {
		return moduleRepository.findAll();
	}

	public ModuleEntity getById(Long id) {
		Optional<ModuleEntity> optional = moduleRepository.findById(id);
		return optional.isPresent() ? optional.get() : ModuleEntity.builder().build();
	}

	public ModuleEntity update(ModuleDto tagDto, Long id, HttpServletRequest request) {
		return this.save(tagDto.toBuilder().id(id).build(),request);
	}

	public ModuleEntity deleteById(Long id, HttpServletRequest request) {
		ModuleEntity group = this.getById(id);
		moduleRepository.save(group.toBuilder().status(StatusEnum.DELETED.status()).build());
		FeatureEnum feature = FeatureEnum.Module;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature, group.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return group;
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return moduleCriteriaService.pagination(ulrd);
	}

	public boolean delete(List<Long> ids) {
		List<ModuleEntity> list = moduleRepository.findByIdIn(ids);
		moduleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public boolean active(List<Long> ids) {
		List<ModuleEntity> list = moduleRepository.findByIdIn(ids);
		moduleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}
}
