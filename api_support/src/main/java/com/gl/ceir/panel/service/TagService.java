package com.gl.ceir.panel.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.HttpStatusEnum;
import com.gl.ceir.panel.constant.MessaeEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.ModuleTagDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.response.ApiStatusDto;
import com.gl.ceir.panel.dto.response.MessageResponse;
import com.gl.ceir.panel.entity.app.ModuleTagEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.TagRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.TagCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class TagService {
	private final TagRepository tagRepository;
	private final TagCriteriaService tagCriteriaService;
	private final UserRepository userRepository;
	private final AuditTrailService auditTrailService;

	public MessageResponse save(ModuleTagDto tagDto, HttpServletRequest request) {
		ModuleTagEntity tag = null; 
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
			tag = tagRepository.save(ModuleTagEntity.builder().id(tagDto.getId()).moduleTagName(tagDto.getModuleTagName())
					.description(tagDto.getDescription()).createdBy(userEntity.getId()).modifiedBy(userEntity.getId())
					.status(StatusEnum.ACTIVE.status).build());
			FeatureEnum feature = FeatureEnum.Tag;
			ActionEnum action = ObjectUtils.isEmpty(tagDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
			String details = String.format("%s [%s] is %s", feature, tag.getId(), action.getName());
			auditTrailService.audit(request, feature, action, details);
			return MessageResponse.builder().status(HttpStatusEnum.SUCCESS.status).code(HttpStatus.OK).data(tag).build();
		} catch(Exception e) {
			log.error("Error while tag creation:{}", e.getMessage());
			return MessageResponse.builder().status(HttpStatusEnum.FAILED.status).code(HttpStatus.OK)
					.message(e.getMessage().contains("Duplicate") ? MessaeEnum.DUPLICATE.message: MessaeEnum.UNKNOWN.message).data(tag).build();
			
		}
	}

	public List<ModuleTagEntity> getTags() {
		return tagRepository.findAll();
	}

	public ModuleTagEntity getById(Long id) {
		Optional<ModuleTagEntity> optional = tagRepository.findById(id);
		return optional.isPresent() ? optional.get() : ModuleTagEntity.builder().build();
	}

	public MessageResponse update(ModuleTagDto tagDto, Long id, HttpServletRequest request) {
		ModuleTagEntity group = this.getById(id);
		return this.save(tagDto.toBuilder().id(group.getId()).build(),request);
	}

	public ModuleTagEntity deleteById(Long id, HttpServletRequest request) {
		ModuleTagEntity group = this.getById(id);
		tagRepository.save(group.toBuilder().status(StatusEnum.DELETED.status()).build());
		FeatureEnum feature = FeatureEnum.Tag;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature, group.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return group;
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return tagCriteriaService.pagination(ulrd);
	}

	public boolean delete(List<Long> ids) {
		List<ModuleTagEntity> list = tagRepository.findByIdIn(ids);
		tagRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public boolean active(List<Long> ids) {
		List<ModuleTagEntity> list = tagRepository.findByIdIn(ids);
		tagRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}
}
