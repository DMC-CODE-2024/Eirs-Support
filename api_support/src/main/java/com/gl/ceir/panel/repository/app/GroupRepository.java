package com.gl.ceir.panel.repository.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.GroupEntity;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<GroupEntity, Long>,
		JpaSpecificationExecutor<GroupEntity>, CrudRepository<GroupEntity, Long> {
	public List<GroupEntity> findAll();

	public Set<GroupEntity> findByParentIn(Set<GroupEntity> parents);

	public List<GroupEntity> findByParentIsNull();

	public List<GroupEntity> findByCreatedByInAndStatus(Set<Long> ids, String status);
	
	public List<GroupEntity> findByCreatedByInAndStatusAndParentIsNull(Set<Long> ids, String status);
	
	public List<GroupEntity> findByCreatedByIn(Set<Long> ids);

	public List<GroupEntity> findByParentIn(List<GroupEntity> parents);

	public List<GroupEntity> findByParent(GroupEntity parent);
	
	public List<GroupEntity> findByIdIn(List<Long> ids);
}
