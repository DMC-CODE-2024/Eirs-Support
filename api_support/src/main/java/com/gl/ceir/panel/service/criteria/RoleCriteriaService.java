package com.gl.ceir.panel.service.criteria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.ColumnEnum;
import com.gl.ceir.panel.dto.FilterDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserGroupPermissionDto;
import com.gl.ceir.panel.entity.app.RoleEntity;
import com.gl.ceir.panel.repository.app.RoleRepository;
import com.gl.ceir.panel.service.UserPermissionService;
import com.gl.ceir.panel.util.DateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor()
public class RoleCriteriaService extends AbstractCriteria {
	private final RoleRepository roleRepository;
	private final DateUtil dateUtil;
	private final UserPermissionService userPermissionService;

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return roleRepository.findAll(new Specification<RoleEntity>() {
			UserGroupPermissionDto ugpd = userPermissionService.permissions();
			private static final long serialVersionUID = 6411748625986892210L;

			@Override
			public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				forPermissions(root, criteriaBuilder, predicates, ugpd);
				if (CollectionUtils.isNotEmpty(ulrd.getFilters())) {
					for (FilterDto filter : ulrd.getFilters()) {
						forRoleName(root, criteriaBuilder, predicates, filter)
								.forStartDate(root, criteriaBuilder, predicates, filter)
								.forEndDate(root, criteriaBuilder, predicates, filter);
					}
				}
				sortBy(ulrd, root, query, criteriaBuilder, predicates);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, PageRequest.of(ulrd.getPage().getCurrent() - 1, ulrd.getPage().getSize()));
	}

	private RoleCriteriaService sortBy(PaginationRequestDto ulrd, Root<RoleEntity> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		Path<Object> path = null;
		if(ObjectUtils.isEmpty(ulrd.getSort())) {
			path = root.get(ColumnEnum.UPDATED_ON.column);
		} else {
			if(ulrd.getSort().getBy().equals(ColumnEnum.UPDATED_ON.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if(ulrd.getSort().getBy().equals(ColumnEnum.CREATED_ON.column)) {
				path = root.get(ulrd.getSort().getBy());	
			}else if(ulrd.getSort().getBy().equals(ColumnEnum.STATUS.column)) {
				path = root.get(ulrd.getSort().getBy());	
			}else if(ulrd.getSort().getBy().equals(ColumnEnum.ROLE.column)) {
				path = root.get(ulrd.getSort().getBy());	
			}
		}
		if(ObjectUtils.isEmpty(path)) return this;
		if(ObjectUtils.isNotEmpty(ulrd.getSort()) && ulrd.getSort().isReverse()) query.orderBy(criteriaBuilder.asc(path));
		else query.orderBy(criteriaBuilder.desc(path));
		return this;
	}

	private RoleCriteriaService forRoleName(Root<RoleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.ROLE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get(ColumnEnum.ROLE.column), "%"+filter.getValue()+"%")));
		}
		return this;
	}

	private RoleCriteriaService forStartDate(Root<RoleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.STARTDATE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime startDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get(ColumnEnum.CREATED_ON.column), startDate)));
		}
		return this;
	}

	private RoleCriteriaService forEndDate(Root<RoleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.ENDDATE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime endDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			endDate = endDate.plusHours(24).minusSeconds(1);
			predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get(ColumnEnum.CREATED_ON.column), endDate)));
		}
		return this;
	}
}
