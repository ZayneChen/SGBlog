package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZayneChen
 * @date 2022年09月22日 15:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuTreeVo {
    private List<MenuVo> menus;
    private List<Long> checkedKeys;
}
