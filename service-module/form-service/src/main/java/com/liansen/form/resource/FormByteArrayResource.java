package com.liansen.form.resource;


import com.liansen.common.resource.BaseResource;
import com.liansen.form.constant.ErrorConstant;
import com.liansen.form.domain.ByteArray;
import com.liansen.form.repository.ByteArrayRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cdy
 * @create 2018/9/12
 */
@Api(description = "表单二进制接口")
@RestController
public class FormByteArrayResource extends BaseResource {
    @Autowired
    ByteArrayRepository byteArrayRepository;

    private ByteArray getFormByteArrayFromRequest(Integer id) {
        ByteArray byteArray = byteArrayRepository.findOne(id);
        if (byteArray == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.FORM_TABLE_NOT_FOUND);
        }
        return byteArray;
    }

    @ApiOperation("根据表单Id查看表单二进制文件")
    @GetMapping(value = "/form-byteArray/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ByteArray getFormDefinition(@PathVariable Integer id) {
        return getFormByteArrayFromRequest(id);
    }


}
