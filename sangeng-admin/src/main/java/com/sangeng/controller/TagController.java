package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagVo;
import com.sangeng.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZayneChen
 * @date 2022年09月16日 8:58
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
//        System.out.println(tag);
        tagService.save(tag);
        System.out.println("test");
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delTag(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        return new ResponseResult(200,"操作成功",tag);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }


    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}
