package com.liansen.form.repository;



import com.liansen.common.repository.BaseRepository;
import com.liansen.form.domain.FormLayout;

import java.util.List;

public interface FormLayoutRepository extends BaseRepository<FormLayout, Integer> {
     List<FormLayout> findByTableId(Integer id);
}