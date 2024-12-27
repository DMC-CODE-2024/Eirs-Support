package com.gl.ceir.panel.constant;

public enum ActionEnum {
	Add("added"), Delete("deleted"), Update("updated"), View("viewed"),Active("activated"),ResetPassword("reset"),PasswordChanged("password changed");

	private String name;

	private ActionEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
