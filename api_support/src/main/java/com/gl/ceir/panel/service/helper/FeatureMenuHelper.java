package com.gl.ceir.panel.service.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.dto.response.FeautreMenuDto;
import com.gl.ceir.panel.entity.app.GroupFeatureEntity;
import com.gl.ceir.panel.entity.app.LinkEntity;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.entity.app.UserGroupEntity;
import com.gl.ceir.panel.repository.app.GroupFeatureRepository;
import com.gl.ceir.panel.repository.app.LinkRepository;
import com.gl.ceir.panel.repository.app.UserGroupRepository;
import com.gl.ceir.panel.repository.app.UserRepository;
import com.gl.ceir.panel.security.jwt.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor()
public class FeatureMenuHelper {

	private final UserRepository userRepository;
	private final LinkRepository linkRepository;
	private final GroupFeatureRepository groupFeatureRepository;
	private final UserGroupRepository userGroupRepository;

	public Set<FeautreMenuDto> menu() {
		try {
			UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			UserEntity userEntity = userRepository.findByUserName(user.getUsername()).get();
			List<UserGroupEntity> groups = userGroupRepository.findByIdUserIdAndStatusOrderByDisplayOrder(userEntity.getId(), StatusEnum.ACTIVE.status);
			List<GroupFeatureEntity> features = new ArrayList<GroupFeatureEntity>();
			groups.forEach(g -> {
				List<GroupFeatureEntity> gfeature = groupFeatureRepository.findByIdGroupIdAndStatusOrderByDisplayOrder(g.getId().getGroupId(), StatusEnum.ACTIVE.status);
				features.addAll(gfeature);
			});
			return process(features);
		} catch (Exception e) {
			log.error("Invalid user's token id:{}", e.getMessage());
		}
		return new HashSet<>();
	}

	private Set<FeautreMenuDto> process(List<GroupFeatureEntity> features) {
		Set<FeautreMenuDto> menu = new HashSet<FeautreMenuDto>();
		Map<String, FeautreMenuDto> map = new HashMap<String, FeautreMenuDto>();
		Map<String, LinkEntity> links = links();
		AtomicInteger intobj = new AtomicInteger();
		features.forEach(f -> {
			try {
				menu.add(FeautreMenuDto.builder().featureId(f.getFeature().getId()).name(f.getFeature().getFeatureName())
					.link(links.get(f.getFeature().getLink()).getUrl()).iframeUrl(links.get(f.getFeature().getLink()).getIframeUrl())
					.icon(ObjectUtils.isNotEmpty(f.getFeature().getLogo()) ? f.getFeature().getLogo()
							: links.get(f.getFeature().getLink()).getIcon())
					.key(f.getFeature().getLink()).displayOrder(intobj.getAndIncrement()).build());
			}catch(Exception e) {
				log.warn("Error:{} while create menu:{}", e.getMessage(), f.getFeature().getFeatureName());
			}
		});
		
		menu.forEach(m -> map.put(m.getName(), m));
		return new HashSet<>(map.values());
	}

	private Map<String, LinkEntity> links() {
		Map<String, LinkEntity> map = new HashMap<>();
		linkRepository.findAll().forEach(link -> map.put(link.getLinkName(), link));
		return map;
	}
}
