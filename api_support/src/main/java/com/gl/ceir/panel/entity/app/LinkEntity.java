package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import com.gl.ceir.panel.constant.UrlTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name="link")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LinkEntity extends AbstractTimestampEntity implements Serializable{
	private static final long serialVersionUID = -6525112908440677714L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 32)
	private String linkName;
	@Column(length = 32, unique=true)
	private String url;
	@Column(length = 128, unique=true)
	private String iframeUrl;
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private UrlTypeEnum urlType;
	@Column(length = 32)
	private String icon;
	@Column(name = "created_by", updatable=false)
	private Long createdBy;
	private Long modifiedBy;
}
