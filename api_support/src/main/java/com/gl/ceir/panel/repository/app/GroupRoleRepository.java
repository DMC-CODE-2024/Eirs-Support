package com.gl.ceir.panel.repository.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupRoleEntity;
import com.gl.ceir.panel.entity.app.GroupRoleId;

@Repository
public interface GroupRoleRepository extends PagingAndSortingRepository<GroupRoleEntity, GroupRoleId>,
		JpaSpecificationExecutor<GroupRoleEntity>, CrudRepository<GroupRoleEntity, GroupRoleId> {
	public GroupRoleEntity findByGroup(GroupEntity group);

	public List<GroupRoleEntity> findByIdGroupId(Long groupId);

	public List<GroupRoleEntity> findByIdIn(List<GroupRoleId> ids);

	public List<GroupRoleEntity> findByIdGroupIdAndStatus(Long groupId, String status);
	
	public List<GroupRoleEntity> findByIdRoleIdAndStatus(Long roleId, String status);

	public List<GroupRoleEntity> findByIdGroupIdInAndStatus(Set<Long> groupIds, String status);
}
