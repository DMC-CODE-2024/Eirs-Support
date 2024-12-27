package com.gl.ceir.panel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.panel.dto.CheckImeiRequest;
import com.gl.ceir.panel.dto.DeviceRequest;
import com.gl.ceir.panel.repository.remote.DeviceRepositoryRemote;
import com.gl.ceir.panel.repository.remote.GsmaRepositoryRemote;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@SuppressWarnings("unused")
@RestController
@Log4j2
@RequestMapping("device")
@AllArgsConstructor
public class DeviceController {
	private final DeviceRepositoryRemote deviceRepositoryRemote;
	private final GsmaRepositoryRemote gsmaRepositoryRemote;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("page-view")
	public @ResponseBody Object viewDeviceDetails(@RequestBody DeviceRequest deviceRequest) {
		Object response = deviceRepositoryRemote.deviceManagementFeign(deviceRequest, deviceRequest.getPageNo(),
				deviceRequest.getPageSize(), 0, deviceRequest.getSource());
		return response;
	}
	
	@PostMapping("/checkDevice")
	public ResponseEntity<?> emailExist(@RequestBody CheckImeiRequest checkImeiRequest) {
		return new ResponseEntity<>(gsmaRepositoryRemote.viewDetails(checkImeiRequest), HttpStatus.OK);
	}
}
