package com.gl.ceir.panel.service.criteria;

import java.util.List;

import com.gl.ceir.panel.dto.UserGroupPermissionDto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Log4j2
public abstract class AbstractCriteria {
	protected AbstractCriteria forPermissions(Root<?> root, CriteriaBuilder criteriaBuilder, 
			List<Predicate> predicates, UserGroupPermissionDto ugpd) {
		predicates.add(criteriaBuilder.and(root.get("createdBy").in(ugpd.getUserIds())));
		return this;
	}
	protected AbstractCriteria forRelationPermissions(Root<?> root, CriteriaBuilder criteriaBuilder, 
			List<Predicate> predicates, UserGroupPermissionDto ugpd) {
		predicates.add(criteriaBuilder.and(root.get("group").get("modifiedBy").in(ugpd.getUserIds())));
		return this;
	}
}
