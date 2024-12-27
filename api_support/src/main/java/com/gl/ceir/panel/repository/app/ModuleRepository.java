package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.ModuleEntity;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<ModuleEntity, Long>,
		JpaSpecificationExecutor<ModuleEntity>, CrudRepository<ModuleEntity, Long> {
	public List<ModuleEntity> findAll();

	public List<ModuleEntity> findByIdIn(List<Long> ids);
}
