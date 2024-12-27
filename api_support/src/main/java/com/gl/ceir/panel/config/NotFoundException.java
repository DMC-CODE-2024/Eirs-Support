package com.gl.ceir.panel.config;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = 7076426643826912091L;

	public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "NotFoundException: " + getMessage();
    }

}
