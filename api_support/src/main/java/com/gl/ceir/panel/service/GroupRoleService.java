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
import com.gl.ceir.panel.dto.GroupRoleDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.response.GroupRoleViewDto;
import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupRoleEntity;
import com.gl.ceir.panel.entity.app.GroupRoleId;
import com.gl.ceir.panel.entity.app.RoleEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.GroupRepository;
import com.gl.ceir.panel.repository.app.GroupRoleRepository;
import com.gl.ceir.panel.repository.app.RoleRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.GroupRoleCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class GroupRoleService {
	private final GroupRoleRepository groupRoleRepository;
	private final GroupRoleCriteriaService groupRoleCriteriaService;
	private final GroupRepository groupRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final AuditTrailService auditTrailService;

	public GroupEntity save(GroupRoleDto groupRoleDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();

		Optional<GroupEntity> oue = groupRepository.findById(groupRoleDto.getGroupId());
		GroupEntity groupEntity = oue.get();
		Set<RoleEntity> roles = new HashSet<>();
		groupRoleDto.getRoles().forEach(roleId -> {
			Optional<RoleEntity> role = roleRepository.findById(roleId);
			roles.add(role.get());
		});
		List<GroupRoleEntity> userRoles = groupRoleDto.getRoles().stream()
				.map(r -> GroupRoleEntity.builder().createdBy(userEntity.getId()).status(StatusEnum.ACTIVE.status)
						.modifiedBy(userEntity.getId())
						.id(GroupRoleId.builder().groupId(groupRoleDto.getGroupId()).roleId(r).build())
						.createdOn(LocalDateTime.now()).build())
				.collect(Collectors.toList());
		Set<GroupRoleId> ids = userRoles.stream().map(g -> g.getId()).collect(Collectors.toSet());
		List<GroupRoleEntity> features = groupRoleRepository.findByIdGroupId(groupRoleDto.getGroupId());
		List<GroupRoleEntity> gfIds = features.stream().filter(f -> ids.contains(f.getId()) == false)
				.map(f -> f.toBuilder().status(StatusEnum.INACTIVE.status).build()).collect(Collectors.toList());
		if (ObjectUtils.isNotEmpty(groupRoleDto.getId())) {
			userRoles.stream().map(r -> {
				GroupRoleEntity gr = features.stream().filter(f -> f.getGroup().getId().equals(groupRoleDto.getId())
						&& f.getId().getRoleId().equals(r.getId().getRoleId())).findFirst().orElse(null);
				return r.toBuilder().createdOn(ObjectUtils.isNotEmpty(gr) ? gr.getCreatedOn() : LocalDateTime.now())
						.build();
			}).collect(Collectors.toList());
		}
		groupRoleRepository.saveAll(gfIds);
		groupRoleRepository.saveAll(userRoles);
		
		FeatureEnum feature = FeatureEnum.GroupRole;
		ActionEnum action = ObjectUtils.isEmpty(groupRoleDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature, groupEntity.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		
		return groupEntity;
	}

	public List<GroupRoleViewDto> getById(Long groupId) {
		List<GroupRoleViewDto> rlist = new ArrayList<GroupRoleViewDto>();
		List<GroupRoleEntity> list = groupRoleRepository.findByIdGroupIdAndStatus(groupId, StatusEnum.ACTIVE.status);
		list.forEach(l -> {
			GroupRoleViewDto grvd = GroupRoleViewDto.builder().build();
			BeanUtils.copyProperties(l, grvd);
			rlist.add(grvd);
		});
		return rlist;
	}

	public GroupEntity update(GroupRoleDto groupRoleId, Long id, HttpServletRequest request) {
		return this.save(groupRoleId.toBuilder().groupId(id).build(),request);
	}

	public boolean delete(List<GroupRoleId> ids) {
		List<GroupRoleEntity> list = groupRoleRepository.findByIdIn(ids);
		groupRoleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public boolean active(List<GroupRoleId> ids) {
		List<GroupRoleEntity> list = groupRoleRepository.findByIdIn(ids);
		groupRoleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return groupRoleCriteriaService.pagination(ulrd);
	}
}
