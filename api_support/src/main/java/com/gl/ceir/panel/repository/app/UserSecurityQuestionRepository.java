package com.gl.ceir.panel.repository.app;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.UserSecurityQuestionEntity;
import com.gl.ceir.panel.entity.app.UserSecurityQuestionId;

@Repository
public interface UserSecurityQuestionRepository
		extends PagingAndSortingRepository<UserSecurityQuestionEntity, UserSecurityQuestionId>,
		JpaSpecificationExecutor<UserSecurityQuestionEntity> {

}
