package com.gl.ceir.panel.repository.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.GroupEntity;
import com.gl.ceir.panel.entity.app.GroupFeatureEntity;
import com.gl.ceir.panel.entity.app.GroupFeatureId;

import jakarta.transaction.Transactional;

@Repository
public interface GroupFeatureRepository extends PagingAndSortingRepository<GroupFeatureEntity, GroupFeatureId>,
		JpaSpecificationExecutor<GroupFeatureEntity>, CrudRepository<GroupFeatureEntity, GroupFeatureId> {
	public GroupFeatureEntity findByGroup(GroupEntity group);

	public Set<GroupFeatureEntity> findByGroupIn(Set<GroupEntity> groups);

	public List<GroupFeatureEntity> findByIdGroupId(Long groupId);

	public List<GroupFeatureEntity> findByIdGroupIdAndStatus(Long groupId, String status);

	public List<GroupFeatureEntity> findByIdIn(List<GroupFeatureId> ids);

	public List<GroupFeatureEntity> findByIdGroupIdInAndStatusOrderByDisplayOrder(List<Long> groupId, String status);
	
	public List<GroupFeatureEntity> findByIdGroupIdAndStatusOrderByDisplayOrder(Long groupId, String status);

	@Modifying
	@Transactional
	public int deleteByIdGroupId(Long groupId);
}
