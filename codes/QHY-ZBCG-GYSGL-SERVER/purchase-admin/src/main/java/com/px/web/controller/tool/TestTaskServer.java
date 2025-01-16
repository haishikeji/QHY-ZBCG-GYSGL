package com.px.web.controller.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestTaskServer {

    public void logStr(){
        log.info("******** 测试定时任务  开始");
        log.info("******** testTaskServer.logStr");
        log.info("******** 测试定时任务  结束");
    }

}
