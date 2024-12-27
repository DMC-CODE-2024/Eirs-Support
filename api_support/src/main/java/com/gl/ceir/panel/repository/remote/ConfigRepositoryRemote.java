package com.gl.ceir.panel.repository.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gl.ceir.panel.dto.DeviceRequest;

@Service
@FeignClient(url = "${config.feign.client.path:}", value = "configRepository")
public interface ConfigRepositoryRemote {
	@RequestMapping(value = "/system-configuration", method = RequestMethod.POST)
	public Object deviceManagementFeign(@RequestBody DeviceRequest deviceRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(name = "source", defaultValue = "menu", required = false) String source);

}
