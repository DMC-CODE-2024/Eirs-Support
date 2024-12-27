package com.gl.ceir.panel.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class FileUploadUtil {
	private final DirectoryUtil directoryUtil;

	public String upload(MultipartFile file, String basepath, String logicalDirectory) {
		String logicalpath = null;
		try {
			if (ObjectUtils.isNotEmpty(file) && !file.isEmpty()) {
				logicalpath = directoryUtil.logicalpath(logicalDirectory);
				basepath = basepath + "/" + logicalpath;
				final Path path = Paths.get(basepath);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
				}
				String name = System.currentTimeMillis() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
				Path filePath = path.resolve(name);
				try (InputStream inputStream = file.getInputStream()) {
					Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				}
				logicalpath = logicalpath + "/" + name;
				log.info("Logical path:{},complete path:{}", logicalpath, basepath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logicalpath;
	}
}
