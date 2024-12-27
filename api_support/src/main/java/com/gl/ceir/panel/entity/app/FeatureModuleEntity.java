package com.gl.ceir.panel.entity.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

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

@Entity(name="feature_module")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FeatureModuleEntity implements Serializable{
	private static final long serialVersionUID = 5059599333132626323L;
	@EmbeddedId
	private FeatureModuleId id;
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	private FeatureEntity feature;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "module_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	private ModuleEntity module;
	
	@Column(name = "created_by", updatable=true)
	private Long createdBy;
	private Long modifiedBy;
	
	@Column(length= 10)
	private String typeOfModule;
	private Long relatedFeatureId;
	
	@Column(length=2,columnDefinition = "varchar(2) default '0'")
	private String status;
	
	@Column(name = "created_on", updatable=true, insertable = true)
    private LocalDateTime createdOn;

	@UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime updatedOn;
}
