package com.gl.ceir.panel.repository.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gl.ceir.panel.config.MultipartSupportConfig;

import feign.Response;

@Service
@FeignClient(url = "${eirs.redmine.downloader.api}", value = "downloadRepository", configuration = MultipartSupportConfig.class)
public interface DownloaderRepositoryRemote {
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public Response attachment(@PathVariable(value = "id") String id);
}
