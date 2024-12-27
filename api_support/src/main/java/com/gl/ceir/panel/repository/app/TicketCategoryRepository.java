package com.gl.ceir.panel.repository.app;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.TicketCategoryEntity;

@Repository
public interface TicketCategoryRepository extends PagingAndSortingRepository<TicketCategoryEntity, Calendar>,
		JpaSpecificationExecutor<TicketCategoryEntity>, CrudRepository<TicketCategoryEntity, Calendar> {
	public List<TicketCategoryEntity> findAll();
}