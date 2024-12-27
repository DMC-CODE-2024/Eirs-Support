package com.gl.ceir.downloader.service;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gl.ceir.downloader.entity.AttachmentEntity;
import com.gl.ceir.downloader.repository.AttachmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AttachmentService {
	private final AttachmentRepository attachmentRepository;
	@Value("${redmine.attachment.downloadable.path:}")
	private String directoyPath;
	
	public String getFilePath(Long id) {
		String filepath = "";
		Optional<AttachmentEntity> oattachment = attachmentRepository.findById(id);
		if(oattachment.isPresent()) {
			AttachmentEntity attachment = oattachment.get();
			filepath = directoyPath + attachment.getDiskDirectory() + File.separator + attachment.getDiskFilename();
		}
		log.info("Attachment id:{}, file path: {}", id, filepath);
		return filepath;
	}
}
