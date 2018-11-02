package com.liansen.flow.test;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.access.EjbAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by CDZ on 2018/10/31.
 */
public class StencilSetResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(StencilSetResource.class);

    @Autowired
    protected ObjectMapper objectMapper;

    @RequestMapping(value = "/rest/stencil-sets/editor", method = RequestMethod.GET, produces = "application/json")
    public JsonNode getStencilSetForEditor() {
        try {
            JsonNode stencilNode = objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("stencilset_bpmn.json"));
            return stencilNode;
        } catch (Exception e) {
            LOGGER.error("Error reading bpmn stencil set json", e);
            throw new EjbAccessException("Error reading bpmn stencil set json");
        }
    }
}
