package com.gl.ceir.panel.entity.app;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "user", uniqueConstraints = { @UniqueConstraint(columnNames = "userName") })
@JsonInclude(Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends AbstractTimestampEntity implements Serializable {
	private static final long serialVersionUID = 6609861165334961349L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private UserEntity parent;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	@JsonManagedReference
	private UserProfileEntity profile;
	@Column(length = 100)
	private String password;
	private LocalDateTime passwordDate;
	private int previousStatus;
	private Long referenceId;
	@Column(length = 255)
	private String remark;
	@Column(length=2,columnDefinition = "varchar(3) default 'en'")
	private String userLanguage;
	@Column(length = 50, name="username")
	private String userName;
	private LocalDateTime lastLoginDate;
	private String approvedBy;
	private LocalDateTime approvedDate;
	private int failedAttempt;
	@Column(length=2,columnDefinition = "varchar(2) default '0'")
	private String currentStatus;	
	@Column(name = "created_by", updatable=true)
	private Long createdBy;
	private String modifiedBy;
	private Long userTypeId;
	private int activeSession;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@NotFound(action = NotFoundAction.IGNORE)
	private Set<RoleEntity> roles;
	/*
	@JsonManagedReference
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumns({
        @JoinColumn(name = "user_id", referencedColumnName = "id")
    })
	private java.util.List<UserSecurityQuestionEntity> questions;
	*/
}
