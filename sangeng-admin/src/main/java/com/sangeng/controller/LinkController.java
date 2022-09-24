package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.CategoryDto;
import com.sangeng.domain.entity.Link;
import com.sangeng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZayneChen
 * @date 2022年09月24日 14:49
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult listLink(Integer pageNum, Integer pageSize, Link link){
        return linkService.listLink(pageNum,pageSize,link);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult showLink(@PathVariable Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

}
