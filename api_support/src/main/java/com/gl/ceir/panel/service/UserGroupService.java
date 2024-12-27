package com.gl.ceir.panel.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserCreateDto;
import com.gl.ceir.panel.dto.UserGroupDto;
import com.gl.ceir.panel.dto.response.UserGroupViewDto;
import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupRoleEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserGroupEntity;
import com.gl.ceir.panel.entity.app.UserGroupId;
import com.gl.ceir.panel.mapper.UserGroupMapper;
import com.gl.ceir.panel.repository.app.GroupRepository;
import com.gl.ceir.panel.repository.app.UserGroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.UserGroupCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class UserGroupService {
	private final UserGroupRepository userGroupRepository;
	private final UserGroupCriteriaService userGroupCriteriaService;
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final AuditTrailService auditTrailService;

	public UserEntity save(UserGroupDto userGroupDto, HttpServletRequest request) {
		UserEntity userEntity = null;
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserEntity loggedEntity = userRepository.findByUserName(user.getUsername()).get();

			Optional<UserEntity> oue = userRepository.findById(userGroupDto.getUserId());
			userEntity = oue.get();
			Set<GroupEntity> groups = new HashSet<>();
			userGroupDto.getGroupmap().forEach(roleId -> {
				Optional<GroupEntity> role = groupRepository.findById(roleId.getGroupId());
				groups.add(role.get());
			});
			List<UserGroupEntity> userGroups = userGroupDto.getGroupmap().stream()
					.map(g -> UserGroupEntity.builder().status(StatusEnum.ACTIVE.status)
							.id(UserGroupId.builder().userId(userGroupDto.getUserId()).groupId(g.getGroupId()).build())
							.createdBy(loggedEntity.getId()).modifiedBy(loggedEntity.getId())
							.createdOn(LocalDateTime.now()).displayOrder(g.getDisplayOrder()).build())
					.collect(Collectors.toList());
			Set<UserGroupId> ids = userGroups.stream().map(g -> g.getId()).collect(Collectors.toSet());
			List<UserGroupEntity> features = userGroupRepository.findByIdUserId(userGroupDto.getUserId());
			List<UserGroupEntity> gfIds = features.stream().filter(f -> ids.contains(f.getId()) == false)
					.map(f -> f.toBuilder().status(StatusEnum.INACTIVE.status).build()).collect(Collectors.toList());
			if (ObjectUtils.isNotEmpty(userGroupDto.getId())) {
				userGroups = userGroups.stream().map(r -> {
					UserGroupEntity gr = features.stream().filter(f -> f.getGroup().getId().equals(userGroupDto.getId())
							&& f.getId().getUserId().equals(r.getId().getUserId())).findFirst().orElse(null);
					return r.toBuilder().createdOn(ObjectUtils.isNotEmpty(gr) ? gr.getCreatedOn() : LocalDateTime.now())
							.build();
				}).collect(Collectors.toList());
			}
			userGroupRepository.saveAll(gfIds);
			userGroupRepository.saveAll(userGroups);

			FeatureEnum feature = FeatureEnum.UserGroup;
			ActionEnum action = ObjectUtils.isEmpty(userGroupDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
			String details = String.format("%s [%s] is %s", feature,
					gfIds.stream().map(g -> g.getId().getGroupId()).map(Object::toString).collect(Collectors.joining(",")), action.getName());
			auditTrailService.audit(request, feature, action, details);

		} catch (Exception e) {

		}
		return userEntity;
	}

	public UserEntity update(UserGroupDto userGroupDto, Long id, HttpServletRequest request) {
		return this.save(userGroupDto.toBuilder().id(id).build(), request);
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return userGroupCriteriaService.pagination(ulrd);
	}

	public List<UserGroupEntity> list() {
		return userGroupRepository.findAll();
	}

	public boolean delete(List<UserGroupId> ids, HttpServletRequest request) {
		List<UserGroupEntity> list = userGroupRepository.findByIdIn(ids);
		userGroupRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		FeatureEnum feature = FeatureEnum.UserGroup;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature,
				ids.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return true;
	}

	public boolean active(List<UserGroupId> ids) {
		List<UserGroupEntity> list = userGroupRepository.findByIdIn(ids);
		userGroupRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public com.gl.ceir.panel.dto.response.UserGroupPaginationDto viewById(Long id) {
		Optional<UserEntity> uoptional = userRepository.findById(id);
		com.gl.ceir.panel.dto.response.UserGroupPaginationDto ugpd = com.gl.ceir.panel.dto.response.UserGroupPaginationDto
				.builder().build();
		BeanUtils.copyProperties(uoptional.isPresent() ? uoptional.get() : UserEntity.builder().build(), ugpd);
		return ugpd;
	}

	public List<UserGroupViewDto> getById(Long userId) {
		Optional<UserEntity> ouser = userRepository.findById(userId);
		List<UserGroupEntity> list = userGroupRepository.findByIdUserIdAndStatus(userId, StatusEnum.ACTIVE.status);
		List<UserGroupViewDto> rlist = new ArrayList<UserGroupViewDto>();
		UserCreateDto user = UserCreateDto.builder().build();
		BeanUtils.copyProperties(ouser.get(), user);
		list.forEach(l -> {
			UserGroupViewDto ugvd = UserGroupViewDto.builder().user(user).build();
			BeanUtils.copyProperties(l, ugvd);
			rlist.add(ugvd);
		});
		return rlist;
	}

	public List<UserGroupViewDto> getByUserIdsId(List<Long> userIds) {
		List<UserGroupViewDto> rlist = new ArrayList<UserGroupViewDto>();
		List<UserGroupEntity> list = userGroupRepository.findByIdUserIdInAndStatus(userIds, StatusEnum.ACTIVE.status);
		list.forEach(l -> {
			UserGroupViewDto ugvd = UserGroupViewDto.builder().build();
			BeanUtils.copyProperties(l, ugvd);
			rlist.add(ugvd);
		});
		return rlist;
	}
}
