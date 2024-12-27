package com.gl.ceir.panel.entity.app;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class AbstractTimestampEntity {
	@CreationTimestamp
    @Column(name = "created_on", updatable=false)
    private LocalDateTime createdOn;

	@UpdateTimestamp
    @Column(name = "modifiedOn")
    private LocalDateTime updatedOn;
}
