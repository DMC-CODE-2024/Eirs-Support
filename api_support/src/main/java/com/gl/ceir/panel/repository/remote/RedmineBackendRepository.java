package com.gl.ceir.panel.repository.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gl.ceir.panel.dto.RedmineBackendResponse;

@Service
@FeignClient(url = "${eirs.redmine.url}", value = "redmineBackendRepository")
public interface RedmineBackendRepository {
	@RequestMapping(value = "/users.json", method = RequestMethod.POST)
	public RedmineBackendResponse save(@RequestBody RedmineBackendResponse dto, @RequestHeader HttpHeaders headers);
	
	@RequestMapping(value = "/users/{id}.json", method = RequestMethod.PUT)
	public RedmineBackendResponse update(@PathVariable Long id, @RequestBody RedmineBackendResponse dto, @RequestHeader HttpHeaders headers);

	@RequestMapping(value = "/users.json", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public RedmineBackendResponse view(@RequestParam(value = "name") String username,
			@RequestHeader HttpHeaders headers);
	
	@RequestMapping(value = "/users/{id}.json", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public RedmineBackendResponse viewById(@PathVariable Long id,@RequestHeader HttpHeaders headers);
}
