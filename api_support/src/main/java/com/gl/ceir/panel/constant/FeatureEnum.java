package com.gl.ceir.panel.constant;

public enum FeatureEnum {
	Group("group"), User("user"), Dashboard("dashboard"), Tag("tag"), Module("module"), Feature("feature"),
	FeatureModule("feature-module"), GroupFeature("group-feature"), UserFeature("user-feature"),
	UserGroup("user-group"), Role("role"), UserRole("user-role"), GroupRole("group-role"), Acl("acl"),
	ViewTicket("ticket"), SearchImei("search-imei"), CheckTicketStatus("check-ticket-status"),
	RegisterTicket("register-ticket");

	private String name;

	private FeatureEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
