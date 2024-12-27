package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.constant.UrlTypeEnum;
import com.gl.ceir.panel.entity.app.LinkEntity;

@Repository
public interface LinkRepository extends PagingAndSortingRepository<LinkEntity, Long>,
		JpaSpecificationExecutor<LinkEntity>, CrudRepository<LinkEntity, Long> {
	public List<LinkEntity> findAll();

	public List<LinkEntity> findByUrlType(UrlTypeEnum urlType);
	
	public LinkEntity findOneByUrl(String url);
}
