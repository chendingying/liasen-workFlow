package com.liansen.flow.rest.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.CustomProperty;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.editor.language.json.converter.UserTaskJsonConverter;

import java.util.Map;

/**
 * Created by CDZ on 2018/10/31.
 */
public class CustomUserTaskJsonConverter extends UserTaskJsonConverter {
    @Override
    protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
        FlowElement flowElement = super.convertJsonToElement(elementNode, modelNode, shapeMap);
        UserTask userTask = (UserTask) flowElement;
        //将自己的属性添加到activiti自带的自定义属性中
        CustomProperty customProperty = new CustomProperty();
        customProperty.setName("readusertaskassignment");
        customProperty.setSimpleValue(this.getPropertyValueAsString("readusertaskassignment", elementNode));
        userTask.getCustomProperties().add(customProperty);
        return userTask;
    }

    @Override
    protected void convertElementToJson(ObjectNode propertiesNode, BaseElement baseElement) {
        super.convertElementToJson(propertiesNode, baseElement);
    }
}
