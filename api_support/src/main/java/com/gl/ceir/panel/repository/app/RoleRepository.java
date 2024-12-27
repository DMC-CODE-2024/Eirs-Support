package com.gl.ceir.panel.repository.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.RoleEntity;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Long>,
		JpaSpecificationExecutor<RoleEntity>, CrudRepository<RoleEntity, Long> {
	public List<RoleEntity> findAll();

	public List<RoleEntity> findByIdIn(List<Long> ids);

	public List<RoleEntity> findByCreatedByInAndStatus(Set<Long> ids, String status);
}
