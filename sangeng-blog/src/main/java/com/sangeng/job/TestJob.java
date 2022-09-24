package com.sangeng.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ZayneChen
 * @date 2022年09月14日 10:43
 */
@Component
public class TestJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void testJob(){
        // 要执行的代码
        System.out.println("定时任务执行了");
    }
}
