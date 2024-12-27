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
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.RoleDto;
import com.gl.ceir.panel.entity.app.RoleEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.RoleRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;
import com.gl.ceir.panel.service.criteria.RoleCriteriaService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class RoleService {
	private final RoleRepository roleRepository;
	private final RoleCriteriaService roleCriteriaService;
	private final UserRepository userRepository;
	private final UserPermissionService permissionService;
	private final AuditTrailService auditTrailService;
	
	public RoleEntity save(RoleDto roleDto, HttpServletRequest request) {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
		RoleEntity role = roleRepository.save(RoleEntity.builder().id(roleDto.getId()).roleName(roleDto.getRoleName())
				.description(roleDto.getDescription()).access(roleDto.getAccess())
				.createdBy(userEntity.getId()).modifiedBy(userEntity.getId()).status(StatusEnum.ACTIVE.status).build());
		
		FeatureEnum feature = FeatureEnum.Role;
		ActionEnum action = ObjectUtils.isEmpty(roleDto.getId()) ? ActionEnum.Add : ActionEnum.Update;
		String details = String.format("%s [%s] is %s", feature, role.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		
		return role;
	}
	public List<RoleEntity> getRoles(){
		return roleRepository.findByCreatedByInAndStatus(permissionService.permissions().getUserIds(),
				StatusEnum.ACTIVE.status);
	}
	public RoleEntity getById(Long id) {
		Optional<RoleEntity> optional = roleRepository.findById(id);
		return optional.isPresent() ? optional.get(): RoleEntity.builder().build();
	}
	public RoleEntity update(RoleDto roleDto, Long id, HttpServletRequest request) {
		RoleEntity group = this.getById(id);
		return this.save(roleDto.toBuilder().id(group.getId()).build(),request);
	}
	public RoleEntity deleteById(Long id, HttpServletRequest request) {
		RoleEntity group = this.getById(id);
		roleRepository.deleteById(id);
		FeatureEnum feature = FeatureEnum.Role;
		ActionEnum action = ActionEnum.Delete;
		String details = String.format("%s [%s] is %s", feature, group.getId(), action.getName());
		auditTrailService.audit(request, feature, action, details);
		return group;
	}
	public Page<?> pagination(PaginationRequestDto ulrd) {
		return roleCriteriaService.pagination(ulrd);
	}
	public boolean delete(List<Long> ids) {
		List<RoleEntity> list = roleRepository.findByIdIn(ids);
		roleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.DELETED.status).build())
				.collect(Collectors.toList()));
		return true;
	}

	public boolean active(List<Long> ids) {
		List<RoleEntity> list = roleRepository.findByIdIn(ids);
		roleRepository.saveAll(list.stream().map(l -> l.toBuilder().status(StatusEnum.ACTIVE.status).build())
				.collect(Collectors.toList()));
		return true;
	}
}
