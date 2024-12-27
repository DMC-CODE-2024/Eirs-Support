package com.gl.ceir.panel.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DateUtil {
	public final static String _fronendFormat = "yyyy-MM-dd";
	public final static String _backendFormat = "yyyy-MM-dd";
	
	public LocalDateTime toLocalDateTime(String date, String srcFormat) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(srcFormat);
		return LocalDate.parse(date, formatter).atStartOfDay();
	}
	public LocalDate toLocalDate(String date, String srcFormat) {
		log.info("date:{}, format:{}", date, srcFormat);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(srcFormat);
		return LocalDate.parse(date, formatter);
	}
}
