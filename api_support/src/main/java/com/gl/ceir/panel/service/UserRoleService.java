package com.gl.ceir.panel.service;

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
import com.gl.ceir.panel.dto.UserRoleDto;
import com.gl.ceir.panel.dto.response.UserRoleViewDto;
import com.gl.ceir.panel.entity.app.RoleEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserRoleEntity;
import com.gl.ceir.panel.entity.app.UserRoleId;
import com.gl.ceir.panel.repository.app.RoleRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.repository.app.UserRoleRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.UserRoleCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class UserRoleService {
	private final UserRoleRepository userRoleRepository;
	private final UserRoleCriteriaService userRoleCriteriaService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final AuditTrailService auditTrailService;
	

	public UserEntity save(UserRoleDto userRoleDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity loggedEntity = userRepository.findByUserName(user.getUsername()).get();

		Optional<UserEntity> oue = userRepository.findById(userRoleDto.getUserId());
		UserEntity userEntity = oue.get();
		Set<RoleEntity> roles = new HashSet<>();
		userRoleDto.getRoles().forEach(roleId -> {
			Optional<RoleEntity> role = roleRepository.findById(roleId);
			roles.add(role.get());
		});
		List<UserRoleEntity> userRoles = userRoleDto.getRoles().stream()
				.map(r -> UserRoleEntity.builder().createdBy(loggedEntity.getId()).modifiedBy(loggedEntity.getId())
						.status(StatusEnum.ACTIVE.status)
						.id(UserRoleId.builder().userId(userRoleDto.getUserId()).roleId(r).build()).build())
				.collect(Collectors.toList());
		Set<UserRoleId> ids = userRoles.stream().map(g -> g.getId()).collect(Collectors.toSet());
		List<UserRoleEntity> features = userRoleRepository.findByIdUserId(userRoleDto.getUserId());
		List<UserRoleEntity> gfIds = features.stream().filter(f -> ids.contains(f.getId()) == false)
				.map(f -> f.toBuilder().status(StatusEnum.INACTIVE.status).build()).collect(Collectors.toList());
		userRoleRepository.saveAll(gfIds);
		userRoleRepository.saveAll(userRoles);
		
		FeatureEnum feature = FeatureEnum.UserRole;
		ActionEnum action = ObjectUtils.isEmpty(userRoleDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature,
				gfIds.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
		auditTrailService.audit(request, feature, action, details);
		
		return userEntity;
	}

	public UserEntity update(UserRoleDto userRoleId, Long id, HttpServletRequest request) {
		return this.save(userRoleId.toBuilder().userId(id).build(),request);
	}

	public boolean delete(List<UserRoleId> ids, HttpServletRequest request) {
		List<UserRoleEntity> list = userRoleRepository.findByIdIn(ids);
		userRoleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		FeatureEnum feature = FeatureEnum.UserRole;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature,
				ids.stream().map(Object::toString).collect(Collectors.joining(",")), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return true;
	}

	public boolean active(List<UserRoleId> ids) {
		List<UserRoleEntity> list = userRoleRepository.findByIdIn(ids);
		userRoleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return userRoleCriteriaService.pagination(ulrd);
	}

	public List<UserRoleViewDto> getById(Long userId) {
		List<UserRoleViewDto> rlist = new ArrayList<UserRoleViewDto>();
		List<UserRoleEntity> list = userRoleRepository.findByIdUserIdAndStatus(userId, "1");
		list.forEach(l -> {
			UserRoleViewDto urvd = UserRoleViewDto.builder().build();
			BeanUtils.copyProperties(l, urvd);
			rlist.add(urvd);
		});
		return rlist;
	}
}
