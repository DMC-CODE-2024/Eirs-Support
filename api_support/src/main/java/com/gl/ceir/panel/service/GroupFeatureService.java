package com.gl.ceir.panel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.ActionEnum;
import com.gl.ceir.panel.constant.FeatureEnum;
import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.GroupFeatureDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.response.GroupFeatureViewDto;
import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupFeatureEntity;
import com.gl.ceir.panel.entity.app.GroupFeatureId;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.FeatureRepository;
import com.gl.ceir.panel.repository.app.GroupFeatureRepository;
import com.gl.ceir.panel.repository.app.GroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.GroupFeatureCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class GroupFeatureService {
	private final GroupFeatureRepository groupFeatureRepository;
	private final GroupFeatureCriteriaService groupFeatureCriteriaService;
	private final GroupRepository groupRepository;
	private final FeatureRepository featureRepository;
	private final UserRepository userRepository;
	private final AuditTrailService auditTrailService;
	
	public GroupEntity save(GroupFeatureDto groupFeatureDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
		Optional<GroupEntity> oue = groupRepository.findById(groupFeatureDto.getGroupId());
		GroupEntity groupEntity = oue.get();
		List<GroupFeatureEntity> groupFeatures = groupFeatureDto.getFeaturemap().stream()
				.map(r -> GroupFeatureEntity.builder().createdBy(userEntity.getId()).modifiedBy(userEntity.getId()).status(StatusEnum.ACTIVE.status)
						.id(GroupFeatureId.builder().groupId(groupFeatureDto.getGroupId()).featureId(r.getFeatureId()).build())
						.displayOrder(r.getDisplayOrder()).build())
				.collect(Collectors.toList());
		log.info("group feature:{}", groupFeatures);
		Set<GroupFeatureId> ids = groupFeatures.stream().map(g -> g.getId()).collect(Collectors.toSet());
		List<GroupFeatureEntity> features = groupFeatureRepository.findByIdGroupId(groupFeatureDto.getGroupId());
		List<GroupFeatureEntity> gfIds = features.stream().filter(f -> ids.contains(f.getId()) == false)
				.map(f -> f.toBuilder().status(StatusEnum.INACTIVE.status).build()).collect(Collectors.toList());
		groupFeatureRepository.saveAll(gfIds);
		groupFeatureRepository.saveAll(groupFeatures);
		
		FeatureEnum feature = FeatureEnum.GroupFeature;
		ActionEnum action = ObjectUtils.isEmpty(groupFeatureDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature, groupEntity.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		
		return groupEntity;
	}

	public GroupEntity update(GroupFeatureDto userGroupDto, Long id, HttpServletRequest request) {
		return this.save(userGroupDto.toBuilder().id(id).build(),request);
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return groupFeatureCriteriaService.pagination(ulrd);
	}

	public boolean delete(List<GroupFeatureId> ids) {
		List<GroupFeatureEntity> list = groupFeatureRepository.findByIdIn(ids);
		groupFeatureRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}
	public boolean active(List<GroupFeatureId> ids) {
		List<GroupFeatureEntity> list = groupFeatureRepository.findByIdIn(ids);
		groupFeatureRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public List<GroupFeatureViewDto> getById(Long groupId) {
		List<GroupFeatureViewDto> rlist = new ArrayList<GroupFeatureViewDto>();
		List<GroupFeatureEntity> list = groupFeatureRepository.findByIdGroupIdAndStatus(groupId,StatusEnum.ACTIVE.status);
		list.forEach(l -> {
			GroupFeatureViewDto gfvd = GroupFeatureViewDto.builder().build();
			BeanUtils.copyProperties(l , gfvd);
			rlist.add(gfvd);
		});
		return rlist;
	}
}
