package com.liansen.form.repository;


import com.liansen.common.repository.BaseRepository;
import com.liansen.form.domain.FormField;

import java.util.List;

public interface FormFieldRepository extends BaseRepository<FormField, Integer> {
    List<FormField> findByTableId(int tableId);
}