package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.SecurityQuestionEntity;

@Repository
public interface SecurityQuestionRepository extends PagingAndSortingRepository<SecurityQuestionEntity, Long>, JpaSpecificationExecutor<SecurityQuestionEntity>{
	public List<SecurityQuestionEntity> findAll();
}
