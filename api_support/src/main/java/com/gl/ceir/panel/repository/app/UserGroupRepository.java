package com.gl.ceir.panel.repository.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.UserGroupEntity;
import com.gl.ceir.panel.entity.app.UserGroupId;

@Repository
public interface UserGroupRepository extends PagingAndSortingRepository<UserGroupEntity, UserGroupId>,
		JpaSpecificationExecutor<UserGroupEntity>, CrudRepository<UserGroupEntity, UserGroupId> {
	List<UserGroupEntity> findAll();

	public List<UserGroupEntity> findByIdIn(List<UserGroupId> ids);

	public List<UserGroupEntity> findByIdInAndStatus(List<UserGroupId> ids, String status);

	public List<UserGroupEntity> findByIdUserIdAndStatus(Long userId, String status);
	
	public List<UserGroupEntity> findByIdUserIdAndStatusOrderByDisplayOrder(Long userId, String status);

	public List<UserGroupEntity> findByIdUserIdInAndStatus(List<Long> userIds, String status);

	public List<UserGroupEntity> findByIdUserId(Long userId);

	public List<UserGroupEntity> findByIdGroupIdIn(Set<Long> groupIds);
	
	public List<UserGroupEntity> findByIdGroupId(Long groupId);
}
