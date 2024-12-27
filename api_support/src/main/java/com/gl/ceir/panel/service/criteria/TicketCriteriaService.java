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

import com.gl.ceir.panel.dto.FilterDto;
import com.gl.ceir.panel.dto.PaginationRequestDto;
import com.gl.ceir.panel.dto.UserGroupPermissionDto;
import com.gl.ceir.panel.entity.app.TicketEntity;
import com.gl.ceir.panel.repository.app.TicketRepository;
import com.gl.ceir.panel.service.UserPermissionService;
import com.gl.ceir.panel.util.DateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor()
@Log4j2
public class TicketCriteriaService extends AbstractCriteria {
	private final TicketRepository ticketRepository;
	private final DateUtil dateUtil;
	private final UserPermissionService userPermissionService;

	public Page<?> pagination(PaginationRequestDto ulrd) {
		return ticketRepository.findAll(new Specification<TicketEntity>() {
			UserGroupPermissionDto ugpd = userPermissionService.permissions();
			private static final long serialVersionUID = 6411748625986892210L;

			@Override
			public Predicate toPredicate(Root<TicketEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				forPermissions(root, criteriaBuilder, predicates, ugpd);
				if (CollectionUtils.isNotEmpty(ulrd.getFilters())) {
					for (FilterDto filter : ulrd.getFilters()) {
						forPhone(root, criteriaBuilder, predicates, filter)
						.forRaisedBy(root, criteriaBuilder, predicates, filter)
						.forTicket(root, criteriaBuilder, predicates, filter)
								.forStartDate(root, criteriaBuilder, predicates, filter)
								.forEndDate(root, criteriaBuilder, predicates, filter);
					}
				}
				sortBy(ulrd, root, query, criteriaBuilder, predicates);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, PageRequest.of(ulrd.getPage().getCurrent() - 1, ulrd.getPage().getSize()));
	}

	private TicketCriteriaService sortBy(PaginationRequestDto ulrd, Root<TicketEntity> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		if (ObjectUtils.isEmpty(ulrd.getSort()))
			return this;
		Path<Object> path = null;
		if (ulrd.getSort().getBy().equals("roleName") || ulrd.getSort().getBy().equals("roleName")) {
			path = root.get("profile").get(ulrd.getSort().getBy());
			query.orderBy(criteriaBuilder.desc(root.get(ulrd.getSort().getBy())));
		} else if (ulrd.getSort().getBy().equals("createdOn")) {
			path = root.get(ulrd.getSort().getBy());
		}
		if (ObjectUtils.isEmpty(path))
			return this;
		if (ulrd.getSort().isReverse())
			query.orderBy(criteriaBuilder.asc(path));
		else
			query.orderBy(criteriaBuilder.desc(path));

		return this;
	}

	private TicketCriteriaService forPhone(Root<TicketEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals("mobileNumber") && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.get(filter.getProperty()), "%" + filter.getValue() + "%")));
		}
		return this;
	}
	private TicketCriteriaService forTicket(Root<TicketEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals("id") && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.get(filter.getProperty()), "%" + filter.getValue() + "%")));
		}
		return this;
	}
	private TicketCriteriaService forRaisedBy(Root<TicketEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals("raisedBy") && ObjectUtils.isNotEmpty(filter.getValue())) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.get(filter.getProperty()), "%" + filter.getValue() + "%")));
		}
		return this;
	}

	private TicketCriteriaService forStartDate(Root<TicketEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals("startDate") && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime startDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdOn"), startDate)));
		}
		return this;
	}

	private TicketCriteriaService forEndDate(Root<TicketEntity> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates, FilterDto filter) {
		if (filter.getProperty().equals("endDate") && ObjectUtils.isNotEmpty(filter.getValue())) {
			LocalDateTime endDate = dateUtil.toLocalDateTime(filter.getValue(), DateUtil._fronendFormat);
			predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdOn"), endDate)));
		}
		return this;
	}
}
