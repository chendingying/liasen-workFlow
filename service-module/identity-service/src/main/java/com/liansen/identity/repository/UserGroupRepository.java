package com.liansen.identity.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.identity.domain.UserGroup;
import org.hibernate.mapping.List;
import org.springframework.transaction.annotation.Transactional;

public interface UserGroupRepository extends BaseRepository<UserGroup, Integer> {

	@Transactional
	int deleteByUserId(int userId);

	@Transactional
	int deleteByGroupIdAndUserId(int groupId, int userId);
}