package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto implements Serializable{
	private static final long serialVersionUID = 3036292616916071914L;
	private String channelType;
	private String email;
	private String message;
	private String subject;
	@Default
	private String msgLang = "en";
	private String msisdn;
	@Default
	private String operatorName = "";
	private String featureName;
	private String featureTxnId;
	private Long featureId;
	private Long userId;
}
