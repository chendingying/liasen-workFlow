package com.liansen.identity.repository;

import com.liansen.common.repository.BaseRepository;
import com.liansen.identity.domain.ModuleType;

import java.util.List;

/**
 * Created by CDZ on 2018/9/18.
 */
public interface ModuleTypeRepository extends BaseRepository<ModuleType, Integer> {
    List<ModuleType> findByParentId(int parentId);
    ModuleType findByModuleTypeAndParentId(String name, Integer parentId);

}
