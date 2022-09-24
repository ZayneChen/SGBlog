package com.sangeng.job;

import com.sangeng.domain.entity.Article;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ZayneChen
 * @date 2022年09月14日 14:46
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/55 * * * * ?")
    public void updateViewCount(){
        // 获取redis中的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");
        Article article = new Article();
        // 更新到数据库中
        for (Map.Entry<String, Integer> entry : cacheMap.entrySet()) {
            article.setId(Long.valueOf(entry.getKey()));
            article.setViewCount(Long.valueOf(entry.getValue()));
            articleService.updateById(article);
        }

    }
}
