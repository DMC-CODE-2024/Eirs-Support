package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.ModuleTagEntity;

@Repository
public interface TagRepository extends PagingAndSortingRepository<ModuleTagEntity, Long>,
		JpaSpecificationExecutor<ModuleTagEntity>, CrudRepository<ModuleTagEntity, Long> {
	public List<ModuleTagEntity> findAll();

	public List<ModuleTagEntity> findByIdIn(List<Long> ids);
}
