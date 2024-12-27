package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "role_feature_module_access")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleFeatureModuleAccessEntity extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 7944319811942160951L;
	@EmbeddedId
	private RoleFeatureModuleAccessId id;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	@JsonManagedReference
	private RoleEntity role;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	@JsonManagedReference
	private FeatureEntity feature;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "module_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	@JsonManagedReference
	private ModuleEntity module;
	
	@Column(length = 255)
	private String featureModuleOverrideLink;
	@Column(name = "created_by", updatable=false)
	private Long createdBy;
	private Long modifiedBy;
	@Column(length=2,columnDefinition = "varchar(2) default '0'")
	private String status;
}
