package com.liansen.identity.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.identity.constant.TableConstant;
import com.liansen.identity.domain.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, Integer> {
	@Query("select a from Role a, UserRole b where a.id = b.roleId and b.userId = ?1 ")
	List<Role> findByUserId(int userId);

	@Query("select a from Role a, RoleMenu b where a.id = b.roleId and b.menuId = ?1 ")
	List<Role> findByMenuId(int menuId);

	List<Role> findByStatus(byte status);
}