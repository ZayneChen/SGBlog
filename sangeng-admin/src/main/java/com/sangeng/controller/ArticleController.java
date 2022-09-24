package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        return articleService.articlesList(pageNum,pageSize,articleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult queryArticleById(@PathVariable Long id){
        Article article = articleService.getById(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article){
        articleService.updateById(article);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }

}
