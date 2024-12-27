package com.gl.ceir.panel.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("resource")
public class ResourceController {
	@Value("${eirs.panel.source.path:}")
	private String basepath;
	@Value("${eirs.user.manual:}")
	private String usermanual;
	@Value("${ticket.content.disposition:inline}") //inline, attachment
	private String contentDisposition;

	@GetMapping("/path")
	public ResponseEntity<?> image(@RequestParam String resource, HttpServletResponse response) throws FileNotFoundException, IOException {
		Path path = Paths.get(basepath + File.separator + resource);
		boolean exist = Files.exists(path);
		String contentType = Files.probeContentType(path);
		if (exist) {
			File file = new File(basepath + File.separator + resource);
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, ""+contentDisposition+"; filename=" + file.getName());
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");
			ByteArrayResource resourceObj = new ByteArrayResource(Files.readAllBytes(path));
			return ResponseEntity.ok().headers(header).contentLength(file.length())
					.contentType(MediaType.parseMediaType(contentType)).body(resourceObj);
		} else {
			return new ResponseEntity<>("Missing Attachment", HttpStatus.FAILED_DEPENDENCY);
		}
		
	}
	@GetMapping("/usermanual")
	public ResponseEntity<?> usermanul(HttpServletResponse response) throws FileNotFoundException, IOException {
		File file = new File(basepath + File.separator + usermanual);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usermanual.pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
	}
}
