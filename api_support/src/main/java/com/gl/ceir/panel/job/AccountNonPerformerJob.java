package com.gl.ceir.panel.job;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.gl.ceir.panel.constant.StatusEnum;
import com.gl.ceir.panel.entity.app.UserEntity;
import com.gl.ceir.panel.repository.app.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AccountNonPerformerJob {
	private final UserRepository userRepository;
	@Value("${eirs.non.performer.allow.days.for.account:30}")
	private long nonPerformerAllowDaysForAccount;
	
	@Scheduled(cron = "${eirs.account.non.performer.cron:* * */6 * * ?}")
	public void makeInactiveNonPerformerAccount() {
		LocalDateTime filterForLastLoginDate = LocalDateTime.now().minusDays(nonPerformerAllowDaysForAccount);
		List<UserEntity> users = userRepository.findByCurrentStatusInAndLastLoginDateLessThanEqual(
				Arrays.asList(StatusEnum.ACTIVE.status), filterForLastLoginDate);
		log.info("Check user inactivity for:{},non performer user list:{}, since:{} days", filterForLastLoginDate, users.size(), nonPerformerAllowDaysForAccount);
		users.stream().map(u -> u.toBuilder().currentStatus(StatusEnum.DELETED.status).build()).collect(Collectors.toList());
		users.forEach(user -> userRepository.save(user.toBuilder().currentStatus(StatusEnum.DELETED.status).build()));
	}
}
