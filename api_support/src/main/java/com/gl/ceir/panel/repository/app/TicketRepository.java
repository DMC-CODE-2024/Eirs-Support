package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.TicketEntity;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<TicketEntity, String>, JpaSpecificationExecutor<TicketEntity>{
	List<TicketEntity> findByMobileNumber(String mobileNumber);
}
