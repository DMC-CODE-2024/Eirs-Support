package com.gl.ceir.panel.config;

public class BadRequestException extends Exception {

    private static final long serialVersionUID = -4563968754020146800L;

	public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "BadRequestException: "+getMessage();
    }

}
