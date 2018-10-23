package com.liansen.form.resource;


import com.liansen.common.resource.BaseResource;
import com.liansen.form.domain.ByteArray;
import com.liansen.form.repository.ByteArrayRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by CDZ on 2018/9/13.
 */
@Api(description = "表单页面跳转")
@RestController
public class FormEditorResource extends BaseResource {

    @Autowired
    ByteArrayRepository byteArrayRepository;

    @ApiIgnore
    @ApiOperation("根据表单Id跳转表单设计页面")
    @RequestMapping(value = "form-editor/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ByteArray editor(@PathVariable Integer id){
       return byteArrayRepository.findOne(id);
    }
}
