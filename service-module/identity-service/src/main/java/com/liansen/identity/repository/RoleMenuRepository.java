package com.liansen.identity.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.identity.domain.RoleMenu;
import org.springframework.transaction.annotation.Transactional;

public interface RoleMenuRepository extends BaseRepository<RoleMenu, Integer> {

	@Transactional
	int deleteByRoleId(int roleId);

	@Transactional
	int deleteByMenuIdAndRoleId(int menuId, int roleId);

}