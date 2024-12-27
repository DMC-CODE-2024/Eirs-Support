package com.gl.ceir.panel.constant;

public enum ColumnEnum {
	FEATURE("featureName"), STATUS("status"), UPDATED_ON("updatedOn"), CREATED_ON("createdOn"), MODULE(
			"moduleName"), TAG("moduleTagName"), GROUP("groupName"), USER("userName"), GROUP_ID("groupId"), ID(
					"id"), STARTDATE("startDate"), ENDDATE("endDate"), ROLE("roleName"), ROLE_ID(
							"roleId"), FIRST_NAME("firstName"), LAST_NAME(
									"lastName"), COMPANY("companyName"), CURRENT_STATUS("currentStatus");

	public String column;

	ColumnEnum(String column) {
		this.column = column;
	}

	public String column() {
		return this.column;
	}
}
