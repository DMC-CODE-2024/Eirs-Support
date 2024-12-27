package com.gl.ceir.panel.constant;

public enum ColumnPathEnum {
	GROUP("group"), FEATURE("feature"), USER("user"), ROLE("role"), PROFILE("profile"), MODULE("module");

	public String path;

	ColumnPathEnum(String path) {
		this.path = path;
	}

	public String path() {
		return this.path;
	}
}
