package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "user_feature_ip_access_list")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserFeatureIpAccessListEntity extends AbstractTimestampEntity implements Serializable{
	private static final long serialVersionUID = -1555056622843202094L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long featureIpListId;
	private Long userId;
	@Column(length = 500)
	private String ipAddress;
	@Column(name = "created_by", updatable=false)
	private Long createdBy;
	private Long modifiedBy;
}
