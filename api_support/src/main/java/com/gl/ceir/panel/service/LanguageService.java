package com.gl.ceir.panel.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class LanguageService {
	@Value("${eirs.panel.source.path:}")
	private String sourcePath;
	private final static String _LANGUAGE = "language";
	private final ObjectMapper objectMapper;
	private final ResourceLoader resourceLoader;

	public Object languagejson(String language) {
		Object json = null;
		try {
			/*log.info("language:{},path:{}", language,
					(sourcePath + File.separator + _LANGUAGE + File.separator + language));
			json = objectMapper.readValue(new File(sourcePath + File.separator + _LANGUAGE + File.separator + language),
					Object.class);*/
			log.info("Language:{} to read", language);
			return read(language);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	private Object read(String language) {
		Object content = null;
        try {
        	//File file = ResourceUtils.getFile("classpath:language" + File.separator + language);
        	//Resource res = resourceLoader.getResource("classpath:language"+ File.separator + language);
        	InputStream in = this.getClass().getResourceAsStream("/language" + File.separator + language);
        	content = IOUtils.toString(in, StandardCharsets.UTF_8);
        	//content = new String(Files.readAllBytes(res.getFile().toPath()));
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return content;
	}
}
