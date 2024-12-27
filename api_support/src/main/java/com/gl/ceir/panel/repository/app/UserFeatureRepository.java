package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserFeatureEntity;
import com.gl.ceir.panel.entity.app.UserFeatureId;

@Repository
public interface UserFeatureRepository extends PagingAndSortingRepository<UserFeatureEntity, UserFeatureId>,
		JpaSpecificationExecutor<UserFeatureEntity>, CrudRepository<UserFeatureEntity, UserFeatureId> {
	public UserFeatureEntity findByUser(UserEntity user);

	public List<UserFeatureEntity> findByIdIn(List<UserFeatureId> ids);

	public List<UserFeatureEntity> findByIdInAndStatus(List<UserFeatureId> ids, String status);

	public List<UserFeatureEntity> findByIdUserIdAndStatus(Long groupId, String status);

	public List<UserFeatureEntity> findByIdUserId(Long userId);
}
