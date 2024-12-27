package com.gl.ceir.panel.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.dto.LocaleDto;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LocaleService {
	@Value("${GUI_CONFIG_PATH:}/locale")
	private String directory;

	public List<LocaleDto> getLangCodes() throws IOException {
		log.info("Locale directory: {}", directory);
		return Files.list(Paths.get(directory)).map(Path::toFile).filter(File::isFile).map(f -> {
			String code = f.getName().substring(0, f.getName().indexOf("."));
			Locale loc = Locale.forLanguageTag(code);
			String label = loc.getDisplayLanguage();
			return LocaleDto.builder().code(code).label(label).build();
		}).collect(Collectors.toList());
	}
}
