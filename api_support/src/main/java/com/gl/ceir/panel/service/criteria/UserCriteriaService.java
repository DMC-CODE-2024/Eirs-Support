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
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.GroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.service.UserPermissionService;
import com.gl.ceir.panel.util.DateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor()
@Log4j2
@SuppressWarnings("unused")
public class UserCriteriaService extends AbstractCriteria {
	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final DateUtil dateUtil;
	private final UserPermissionService userPermissionService;

	public Page<?> pagination(PaginationRequestDto ulrd) {
		log.info("pagination:{}", ulrd);
		UserGroupPermissionDto ugpd = userPermissionService.permissions();
		return userRepository.findAll(new Specification<UserEntity>() {
			private static final long serialVersionUID = 6411748625986892210L;
			@Override
			public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				forPermissions(root, criteriaBuilder, predicates, ugpd);
				if (CollectionUtils.isNotEmpty(ulrd.getFilters())) {
					for (FilterDto filter : ulrd.getFilters()) {
						forFirstName(root, criteriaBuilder, predicates, filter)
								.forUser(root, criteriaBuilder, predicates, filter)
								.forLastName(root, criteriaBuilder, predicates, filter)
								.forCompanyName(root, criteriaBuilder, predicates, filter)
								.forStartDate(root, criteriaBuilder, predicates, filter)
								.forEndDate(root, criteriaBuilder, predicates, filter);
					}
				}
				sortBy(ulrd, root, query, criteriaBuilder, predicates);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, PageRequest.of(ulrd.getPage().getCurrent() - 1, ulrd.getPage().getSize()));
	}

	private UserCriteriaService sortBy(PaginationRequestDto ulrd, Root<UserEntity> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		Path<Object> path = null;
		if (ObjectUtils.isEmpty(ulrd.getSort())) {
			path = root.get(ColumnEnum.UPDATED_ON.column);
		} else {
			if (ulrd.getSort().getBy().equals(ColumnEnum.UPDATED_ON.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.CREATED_ON.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.CURRENT_STATUS.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.USER.column)) {
				path = root.get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.FIRST_NAME.column)) {
				path = root.get(ColumnPathEnum.PROFILE.path).get(ulrd.getSort().getBy());
			} else if (ulrd.getSort().getBy().equals(ColumnEnum.LAST_NAME.column)) {
				path = root.get(ColumnPathEnum.PROFILE.path).get(ulrd.getSort().getBy());
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

	private UserCriteriaService forFirstName(Root<UserEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.FIRST_NAME.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get(ColumnPathEnum.PROFILE.path).get(ColumnEnum.FIRST_NAME.column), "%"+filter.getValue()+"%")));
		}
		return this;
	}

	private UserCriteriaService forLastName(Root<UserEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.LAST_NAME.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get(ColumnPathEnum.PROFILE.path).get(ColumnEnum.LAST_NAME.column), "%"+filter.getValue()+"%")));
		}
		return this;
	}

	private UserCriteriaService forCompanyName(Root<UserEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals(ColumnEnum.COMPANY.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.get(ColumnPathEnum.PROFILE.path).get(ColumnEnum.COMPANY.column), "%" + filter.getValue() + "%")));
		}
		return this;
	}

	private UserCriteriaService forStartDate(Root<UserEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.STARTDATE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime startDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get(ColumnEnum.CREATED_ON.column), startDate)));
		}
		return this;
	}

	private UserCriteriaService forEndDate(Root<UserEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.ENDDATE.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime endDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			endDate = endDate.plusHours(24).minusSeconds(1);
			predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get(ColumnEnum.CREATED_ON.column), endDate)));
		}
		return this;
	}

	private UserCriteriaService forUser(Root<UserEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if(filter.getProperty().equals(ColumnEnum.USER.column) && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(ColumnEnum.USER.column), "%"+filter.getValue()+"%")));
		}
		return this;
	}
}
