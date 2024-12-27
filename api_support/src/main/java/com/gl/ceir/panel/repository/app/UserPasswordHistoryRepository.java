package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.UserPasswordHistoryEntity;

@Repository
public interface UserPasswordHistoryRepository extends CrudRepository<UserPasswordHistoryEntity, Long>, JpaSpecificationExecutor<UserPasswordHistoryEntity> {
	public List<UserPasswordHistoryEntity> findByUserId(Long id);
}
