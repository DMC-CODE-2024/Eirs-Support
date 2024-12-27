package com.gl.ceir.panel.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrailDto implements Serializable{
	private static final long serialVersionUID = 3036292616916071914L;
	private String browser;
	private String details;
	private Long featureId;
	private String featureName;
	private String jSessionId;
	private String publicIp;
	private String roleType;
	private String subFeature;
	private String txnId;
	private Long userId;
	private String userName;
	private String userType;
	private String userTypeId;
}
