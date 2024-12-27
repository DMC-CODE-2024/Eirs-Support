package com.gl.ceir.panel.entity.app;

import java.io.Serializable;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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

@Entity(name="group_feature")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class GroupFeatureEntity extends AbstractTimestampEntity implements Serializable{
	private static final long serialVersionUID = -249588555492144206L;
	@EmbeddedId
	private GroupFeatureId id;
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	private GroupEntity group;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumns({ @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false, insertable=false, updatable=false) })
	private FeatureEntity feature;
	
	@Column(name = "created_by", updatable=false)
	private Long createdBy;
	private Long modifiedBy;
	@Column(length=2,columnDefinition = "varchar(2) default '0'")
	private String status;
	private int displayOrder;
}
