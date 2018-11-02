package com.liansen.flow.rest.event.resource;

import com.liansen.common.jpa.Criteria;
import com.liansen.common.jpa.Restrictions;
import com.liansen.common.resource.BaseResource;
import com.liansen.common.resource.PageResponse;
import com.liansen.flow.rest.event.EventRequest;
import com.liansen.flow.rest.event.repository.EventResoitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * Created by CDZ on 2018/10/31.
 */
@RestController
public class EventResource extends BaseResource {
    @Autowired
    EventResoitory eventResoitory;

    @PostMapping("/event")
    public void saveEvent(@RequestBody EventRequest eventRequest){
        eventResoitory.save(eventRequest);
    }

    @GetMapping("/event/{id}")
    public EventRequest getEvent(@PathVariable Integer id){
       return eventResoitory.findOne(id);
    }

    @DeleteMapping("/event/{id}")
    public void deleteEvent(@PathVariable Integer id){
        eventResoitory.delete(id);
    }

    @PutMapping("/event/{id}")
    public void putEvent(@PathVariable Integer id,@RequestBody EventRequest event){
        EventRequest eventRequest = getEvent(id);
        if(eventRequest != null){
            eventRequest.setEventName(event.getEventName());
            eventRequest.setRemark(event.getRemark());
            eventRequest.setSql(event.getSql());
            eventResoitory.save(eventRequest);
        }
    }

    @GetMapping("/event")
    public PageResponse findEvent(@ApiIgnore @RequestParam Map<String, String> requestParams){
        Criteria<EventRequest> criteria = new Criteria<EventRequest>();
        criteria.add(Restrictions.eq("id", requestParams.get("id")));
        criteria.add(Restrictions.like("eventName", requestParams.get("eventName")));
        return createPageResponse(eventResoitory.findAll(criteria, getPageable(requestParams)));
    }
}
