package com.gl.ceir.downloader.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.downloader.entity.AttachmentEntity;

@Repository
public interface AttachmentRepository extends CrudRepository<AttachmentEntity, Long>, JpaSpecificationExecutor<AttachmentEntity>  {

}
