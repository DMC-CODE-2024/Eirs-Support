package com.gl.ceir.panel.util;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.entity.app.OperatorSeriesEntity;
import com.gl.ceir.panel.repository.app.OperatorSeriesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class OperatorUtil {
	private final OperatorSeriesRepository operatorSeriesRepository;

	public String getOperator(String msisdn) {
		try {
			String series = msisdn.substring(msisdn.startsWith("+") ? 1: 0, 5);
			if(StringUtils.isNumeric(series)) {
				List<OperatorSeriesEntity> list = operatorSeriesRepository.findSeries(series);
				log.info("Msisdn:{}, series:{}, found list size:{}", msisdn, series, list.size());
				return CollectionUtils.isNotEmpty(list) ? list.get(0).getOperatorName() : "";
			}
			return "";
		} catch (Exception e) {
			log.error("Operator series issue:{} with msisdn:{}", e.getMessage(), msisdn);
			return "";
		}
	}
}
