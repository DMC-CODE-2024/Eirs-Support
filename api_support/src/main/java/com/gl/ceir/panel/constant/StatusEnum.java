package com.gl.ceir.panel.constant;

public enum StatusEnum {
	INACTIVE("0"), ACTIVE("3"), SUSPENDED("4"), LOCKED("5"), DELETED("21");

	public String status;

	StatusEnum(String status) {
		this.status = status;
	}

	public String status() {
		return this.status;
	}
}
