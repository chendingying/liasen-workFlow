package com.liansen.identity.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.identity.domain.UserRole;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleRepository extends BaseRepository<UserRole, Integer> {
	@Transactional
	int deleteByUserId(int userId);

	@Transactional
	int deleteByRoleIdAndUserId(int roleId, int userId);

}