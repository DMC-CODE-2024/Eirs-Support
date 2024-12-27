package com.gl.ceir.panel.repository.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gl.ceir.panel.config.MultipartSupportConfig;
import com.gl.ceir.panel.dto.CheckCountryDto;
import com.gl.ceir.panel.dto.response.CheckCountryResponseDto;

@Service
@FeignClient(url = "${eirs.checkip.country.url}", value = "eirsRepository", configuration = MultipartSupportConfig.class)
public interface CheckIpCountryRemoteRepostiory {
	@RequestMapping(value = "/checkIPCountry", method = RequestMethod.POST)
	public CheckCountryResponseDto check(@RequestBody CheckCountryDto dto);
}
