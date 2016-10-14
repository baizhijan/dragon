package com.bzj.dragon.task;

import com.bzj.dragon.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * User:aaronbai@tcl.com
 * Date:2016-10-14
 * Time:17:18
 */
@Component
public class MyTask {

    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void timeRepeatRun(){

    }

    @Scheduled(fixedDelay =1576800000000L, initialDelay = 1000L)
    public void timeRun(){

    }

    public void AsyncRun(){
        taskService.taskRun();
    }
}
