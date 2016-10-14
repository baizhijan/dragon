package com.bzj.dragon.service.task.impl;

import com.bzj.dragon.service.task.TaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-14
 * Time:17:22
 */
@Service
public class TaskServiceImpl implements TaskService{

    @Async
    @Override
    public void taskRun() {

    }
}
