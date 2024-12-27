package com.gl.ceir.panel.repository.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gl.ceir.panel.dto.CheckImeiRequest;
import com.gl.ceir.panel.dto.ImeiResponse;

@Service
@FeignClient(url="${eirs.gsma.url}",value = "profileUrlsgsma")
public interface GsmaRepositoryRemote {
	@PostMapping(value="/services/checkIMEI/")
	public @ResponseBody ImeiResponse viewDetails(@RequestBody CheckImeiRequest checkImeiRequest);
}
