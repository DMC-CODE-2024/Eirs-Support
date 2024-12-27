package com.gl.ceir.panel.util;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DirectoryUtil {
	@Value("${eirs.panel.source.path:}")
	private String base;

	public String logicalpath(String logicalDirectory) {
		return logicalDirectory + "/" + LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue();
	}
}
