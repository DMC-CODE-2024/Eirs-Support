package com.gl.ceir.panel.entity.app;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name="feature")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonInclude(Include.NON_NULL)
public class FeatureEntity extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 8521209247264150972L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 100)
	private String featureName;
	@Column(length = 255)
	private String description;
	@Column(length = 20)
	private String category;
	@Column(length = 255)
	private String link;
	@Column(length = 255)
	private String logo;
	
	/*
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "default_module_id", referencedColumnName = "id")
	@JsonManagedReference
	@JsonIgnore
    private ModuleEntity defaultModule;
    */
	
	@Column(name = "created_by", updatable=false)
	private Long createdBy;
	private Long modifiedBy;
	@Column(columnDefinition = "integer default 0")
	private int displayOrder;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "feature_module", joinColumns = @JoinColumn(insertable = false, updatable = false, name = "feature_id"), inverseJoinColumns = @JoinColumn(name = "module_id"))
	private List<ModuleEntity> modules;
	
	/*
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_tag_id")
	private ModuleTagEntity moduleTag;
	*/
	@Column(length=2,columnDefinition = "varchar(2) default '0'")
	private String status;
}
