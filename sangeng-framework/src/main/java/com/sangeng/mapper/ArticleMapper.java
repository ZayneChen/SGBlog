package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ZayneChen
 * @date 2022年09月01日 19:31
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
