package com.gl.ceir.panel.repository.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.RoleFeatureModuleAccessEntity;
import com.gl.ceir.panel.entity.app.RoleFeatureModuleAccessId;

import jakarta.transaction.Transactional;

@Repository
public interface AclRepository
		extends PagingAndSortingRepository<RoleFeatureModuleAccessEntity, RoleFeatureModuleAccessId>,
		JpaSpecificationExecutor<RoleFeatureModuleAccessEntity>,
		CrudRepository<RoleFeatureModuleAccessEntity, RoleFeatureModuleAccessId> {
	public List<RoleFeatureModuleAccessEntity> findByIdRoleId(Long roleId);

	public List<RoleFeatureModuleAccessEntity> findByIdRoleIdAndStatus(Long roleId, String status);

	public List<RoleFeatureModuleAccessEntity> findByIdRoleIdInAndStatus(Set<Long> roles, String status);

	public List<RoleFeatureModuleAccessEntity> findAll();

	public void deleteByIdRoleId(Long roleId);

	@Transactional
	public void deleteByIdIn(List<RoleFeatureModuleAccessId> roleIds);

	public List<RoleFeatureModuleAccessEntity> findByIdIn(List<RoleFeatureModuleAccessId> roleIds);

}
