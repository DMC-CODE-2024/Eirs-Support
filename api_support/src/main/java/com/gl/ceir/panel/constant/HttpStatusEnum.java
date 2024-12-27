package com.gl.ceir.panel.constant;

public enum HttpStatusEnum {
	FAILED("failed"),SUCCESS("success");

	public String status;

	HttpStatusEnum(String status) {
		this.status = status;
	}

	public String status() {
		return this.status;
	}
}
