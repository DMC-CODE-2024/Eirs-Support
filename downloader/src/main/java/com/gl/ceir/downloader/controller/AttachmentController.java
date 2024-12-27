package com.gl.ceir.downloader.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.downloader.service.AttachmentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("attachment")
@RequiredArgsConstructor
public class AttachmentController {
	private final AttachmentService attachmentService;
	@Value("${ticket.content.disposition:inline}") //inline, attachment
	private String contentDisposition;

	@GetMapping("{id}")
	public ResponseEntity<?> image(@PathVariable Long id, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		String filepath = attachmentService.getFilePath(id);
		Path path = Paths.get(filepath);
		boolean exist = Files.exists(path);
		String contentType = Files.probeContentType(path);
		log.info("Attachment id:{}, filepath:{}, file exist:{}, content type:{}", id, filepath, exist, contentType);
		if (exist) {
			File file = new File(filepath);
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, ""+contentDisposition+"; filename=" + file.getName());
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

			return ResponseEntity.ok().headers(header).contentLength(file.length())
					.contentType(MediaType.parseMediaType(contentType)).body(resource);
		} else {
			return new ResponseEntity<>("Missing Attachment", HttpStatus.FAILED_DEPENDENCY);
		}
	}
}
