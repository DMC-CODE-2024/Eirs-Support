package com.gl.ceir.panel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.UrlTypeEnum;
import com.gl.ceir.panel.entity.app.LinkEntity;
import com.gl.ceir.panel.repository.app.LinkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@Log4j2
@RequiredArgsConstructor()
public class LinkService {
	private final LinkRepository linkRepository;
	

	public List<LinkEntity> links() {
		return linkRepository.findAll();
	}
	public List<LinkEntity> linksByUrlType(UrlTypeEnum urlTypeEnum) {
		List<LinkEntity> links= linkRepository.findByUrlType(urlTypeEnum);
		return linkRepository.findByUrlType(urlTypeEnum);
	}
	public LinkEntity findByUrl(String url) {
		return linkRepository.findOneByUrl(url);
	}
	public LinkEntity save(LinkEntity linkEntity) {
		return linkRepository.save(linkEntity);
	}
}
