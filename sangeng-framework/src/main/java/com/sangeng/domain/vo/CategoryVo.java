package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZayneChen
 * @date 2022年09月02日 10:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {

    private Long id;
    private String name;

    //描述
    private String description;

}
