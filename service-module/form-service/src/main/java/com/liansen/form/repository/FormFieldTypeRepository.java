package com.liansen.form.repository;


import com.liansen.common.repository.BaseRepository;
import com.liansen.form.domain.FormFieldType;

/**
 * Created by CDZ on 2018/9/21.
 */
public interface FormFieldTypeRepository extends BaseRepository<FormFieldType, Integer> {
    FormFieldType findByName(String name);
}
