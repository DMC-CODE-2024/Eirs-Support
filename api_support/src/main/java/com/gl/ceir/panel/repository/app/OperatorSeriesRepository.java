package com.gl.ceir.panel.repository.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.panel.entity.app.OperatorSeriesEntity;

@Repository
public interface OperatorSeriesRepository extends CrudRepository<OperatorSeriesEntity, Long>, JpaSpecificationExecutor<OperatorSeriesEntity> {
	@Query("select a from OperatorSeriesEntity a where a.seriesStart <= :series and a.seriesEnd >= :series and seriesType='msisdn'")
	public List<OperatorSeriesEntity> findSeries(String series);
}