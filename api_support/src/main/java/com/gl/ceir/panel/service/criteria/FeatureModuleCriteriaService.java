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
import com.gl.ceir.panel.constant.ColumnPathEnum;
import com.gl.ceir.panel.dto.FilterDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserGroupPermissionDto;
import com.gl.ceir.panel.entity.app.FeatureModuleEntity;
import com.gl.ceir.panel.repository.app.FeatureModuleRepository;
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
public class FeatureModuleCriteriaService extends AbstractCriteria {
	private final FeatureModuleRepository featureModuleRepository;
	private final DateUtil dateUtil;
	@SuppressWarnings("unused")
	private final UserPermissionService userPermissionService;

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return featureModuleRepository.findAll(new Specification<FeatureModuleEntity>() {
			UserGroupPermissionDto ugpd = userPermissionService.permissions();
			private static final long serialVersionUID = 6411748625986892210L;

			@Override
			public Predicate toPredicate(Root<FeatureModuleEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				forPermissions(root, criteriaBuilder, predicates, ugpd);
				if (CollectionUtils.isNotEmpty(ulrd.getFilters())) {
					for (FilterDto filter : ulrd.getFilters()) {
						forFeature(root, criteriaBuilder, predicates, filter)
								.forModule(root, criteriaBuilder, predicates, filter)
								.forStartDate(root, criteriaBuilder, predicates, filter)
								.forEndDate(root, criteriaBuilder, predicates, filter);
					}
				}
				sortBy(ulrd, root, query, criteriaBuilder, predicates);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, PageRequest.of(ulrd.getPage().getCurrent() - 1, ulrd.getPage().getSize()));
	}

	private FeatureModuleCriteriaService sortBy(PaginationRequestDto ulrd, Root<FeatureModuleEntity> root,
			CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		Path<Object> path = null;
		if (ObjectUtils.isEmpty(ulrd.getSort())) {
			path = root.get(ColumnEnum.UPDATED_ON.column);
		} else {
			if (ulrd.getSort().getBy().equals(ColumnEnum.UPDATED_ON.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.CREATED_ON.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.STATUS.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.FEATURE.column)) {
				path = root.get(ColumnPathEnum.FEATURE.path).get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.MODULE.column)) {
				path = root.get(ColumnPathEnum.MODULE.path).get(ulrd.getSort().getBy());
			}
		}
		if (ObjectUtils.isEmpty(path))
			return this;
		if (ObjectUtils.isNotEmpty(ulrd.getSort()) && ulrd.getSort().isReverse())
			query.orderBy(criteriaBuilder.asc(path));
		else
			query.orderBy(criteriaBuilder.desc(path));
		return this;
	}

	private FeatureModuleCriteriaService forFeature(Root<FeatureModuleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals(ColumnEnum.FEATURE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.get(ColumnPathEnum.FEATURE.path).get(ColumnEnum.FEATURE.column),
							"%" + filter.getValue() + "%")));
		}
		return this;
	}

	private FeatureModuleCriteriaService forModule(Root<FeatureModuleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals(ColumnEnum.MODULE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.get(ColumnPathEnum.MODULE.path).get(ColumnEnum.MODULE.column),
							"%" + filter.getValue() + "%")));
		}
		return this;
	}

	private FeatureModuleCriteriaService forStartDate(Root<FeatureModuleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals(ColumnEnum.STARTDATE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime startDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.greaterThanOrEqualTo(root.get(ColumnEnum.CREATED_ON.column), startDate)));
		}
		return this;
	}

	private FeatureModuleCriteriaService forEndDate(Root<FeatureModuleEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals(ColumnEnum.ENDDATE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime endDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			endDate = endDate.plusHours(24).minusSeconds(1);
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.lessThanOrEqualTo(root.get(ColumnEnum.CREATED_ON.column), endDate)));
		}
		return this;
	}
}
