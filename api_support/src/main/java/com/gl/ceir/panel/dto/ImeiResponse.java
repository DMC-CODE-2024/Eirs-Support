package com.gl.ceir.panel.dto;

public class ImeiResponse {
		
	private String statusCode;
	private String statusMessage;
	private String language;
	public Object result;
	public Object deviceDetails;
	private String tag;
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Object getDeviceDetails() {
		return deviceDetails;
	}
	public void setDeviceDetails(Object deviceDetails) {
		this.deviceDetails = deviceDetails;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImeiResponse [statusCode=");
		builder.append(statusCode);
		builder.append(", statusMessage=");
		builder.append(statusMessage);
		builder.append(", language=");
		builder.append(language);
		builder.append(", result=");
		builder.append(result);
		builder.append(", deviceDetails=");
		builder.append(deviceDetails);
		builder.append(", validImei=");
		builder.append(", tag=");
		builder.append(tag);
		builder.append("]");
		return builder.toString();
	}
	
	
		
}
