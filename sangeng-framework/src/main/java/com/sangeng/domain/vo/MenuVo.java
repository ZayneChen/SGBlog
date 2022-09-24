package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZayneChen
 * @date 2022年09月22日 10:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuVo {
    private List<MenuVo> children;
    private Long id;
    private String label;
    private Long parentId;
}
