package com.liansen.flow.rest.task;

import org.apache.commons.io.FileUtils;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by CDZ on 2018/11/13.
 */
public class AutodeployProcessBpmnAndImageUtils {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    private final String START_EVENT = "start";
    private final String END_EVENT = "end";


    /**
     * 创建节点任务 个人任务
     * @param id 任务id标识
     * @param name 任务名称
     * @param assignee 指定个人任务
     * @return
     */
    public UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }
    /**
     * 创建节点任务 多人任务
     * @param id 任务id标识
     * @param name 任务名称
     * @param candidateUsers 任务人的集合
     * @return
     */
    public UserTask createUserTask(String id, String name, String[] candidateUsers) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        if(null!=candidateUsers&&candidateUsers.length>0){
            userTask.setCandidateUsers(Arrays.asList(candidateUsers));
        }
        return userTask;
    }

    /**
     * 创建节点任务 使用监听设置处理人
     * @param id 任务id标识
     * @param name 任务名称
     * @param taskListenerList 监听的集合,TaskListener实现类的的具体路径例：com.sky.bluesky.activiti.utils.MangerTaskHandlerCandidateUsers
     * @return
     */
    public UserTask createUserTask(String id, String name, List<String> taskListenerList) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);


        List<FlowableListener> list = new ArrayList<FlowableListener>();
        for (String taskListener : taskListenerList) {
            FlowableListener listener = new FlowableListener();
            listener.setEvent("create");
//Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
            listener.setImplementationType("class");
            listener.setImplementation(taskListener);

            list.add(listener);

        }
        userTask.setTaskListeners(list);
        return userTask;
    }

    /**
     * 设置连线
     * @param from 从哪里出发
     * @param to 连接到哪里
     * @return
     */
    public SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    /**
     * 设置起始节点
     * @return
     */
    public StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(START_EVENT);
        return startEvent;
    }

    /**
     * 排他网关节点
     * @param id
     * @return
     */
    public static ExclusiveGateway createExclusiveGateway(String id) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        return exclusiveGateway;
    }

    /**
     * 设置结束节点
     * @return
     */
    public EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(END_EVENT);
        return endEvent;
    }

    /**
     * 设置连线
     * @param from 从哪里出发
     * @param to 连接到哪里
     * @param name 连线名称
     * @param conditionExpression 判断条件${arg>2}
     * @return
     */
    public static SequenceFlow createSequenceFlow(String from, String to,String name,String conditionExpression) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setName(name);
        if(null!=conditionExpression&&!"".equals(conditionExpression)){
            flow.setConditionExpression(conditionExpression);
        }
        return flow;
    }
    public FlowElement createServiceTask(String name){
        ServiceTask stask = new ServiceTask();
        stask.setId("sid");
        stask.setName(name);
        stask.setImplementation("activitiTest.PrintVariables");
        String implementationType = ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION;
        stask.setImplementationType(implementationType );
        return stask;
    }
}
