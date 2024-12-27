package com.gl.ceir.panel.dto;

public class CheckImeiRequest {
	private String captcha;
	private String imei;
	private String channel;
	private String language;
	private String medium;
	private Integer msisdn;
	private String operator;
	private String createdOn;
	private Integer id;
	private String imeiProcessStatus;
	private String requestProcessStatus;
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public Integer getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(Integer msisdn) {
		this.msisdn = msisdn;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImeiProcessStatus() {
		return imeiProcessStatus;
	}
	public void setImeiProcessStatus(String imeiProcessStatus) {
		this.imeiProcessStatus = imeiProcessStatus;
	}
	public String getRequestProcessStatus() {
		return requestProcessStatus;
	}
	public void setRequestProcessStatus(String requestProcessStatus) {
		this.requestProcessStatus = requestProcessStatus;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CheckImeiRequest [captcha=");
		builder.append(captcha);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", language=");
		builder.append(language);
		builder.append(", medium=");
		builder.append(medium);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", id=");
		builder.append(id);
		builder.append(", imeiProcessStatus=");
		builder.append(imeiProcessStatus);
		builder.append(", requestProcessStatus=");
		builder.append(requestProcessStatus);
		builder.append("]");
		return builder.toString();
	}
	
}