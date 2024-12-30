package com.gl.ceir.panel.constant;

public enum MessaeEnum {
	OLD_PASS_NOT_MATCHED("oldPasswordNotMatched"),
	PASS_HISTORY_MATCHED("oldPasswordHistoryMatched"),
	PASS_CHANGE_SUCCESS("changePasswordSuccess"),
	SAVE_USER_FAILED("saveUserFailed"),
	SAVE_USER_SUCCESS("saveUserSuccess"),
	EMAIL_ALREADY_EXIST("emailAlreadyRegistered"),
	DUPLICATE("duplicateNowAllowed"),
	UNKNOWN("unknown");

	public String message;

	MessaeEnum(String message) {
		this.message = message;
	}

	public String message() {
		return this.message;
	}
}
