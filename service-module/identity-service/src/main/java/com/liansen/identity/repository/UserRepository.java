package com.liansen.identity.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.identity.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Integer> {
	User findByUserName(String userName);

	@Query("select a from User a, UserRole b where a.id = b.userId and b.roleId = ?1 ")
	List<User> findByRoleId(int roleId);

	@Query("select a from User a, UserGroup b where a.id = b.userId and b.groupId = ?1 ")
	List<User> findByGroupId(int groupId);

	@Query("select a.id from User a, UserGroup b where a.id = b.userId and b.groupId = ?1")
	List<Integer> findIdByGroupId(int groupId);
}