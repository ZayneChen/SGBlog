package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZayneChen
 * @date 2022年09月22日 10:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDto {
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String status;
    private List<Long> menuIds;
    private String remark;
}
