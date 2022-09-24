package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZayneChen
 * @date 2022年09月21日 14:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private String title;
    private String summary;
}
