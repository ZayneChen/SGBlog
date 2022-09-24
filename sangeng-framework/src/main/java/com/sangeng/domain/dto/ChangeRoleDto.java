package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZayneChen
 * @date 2022年09月22日 9:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleDto {
    private Long roleId;
    private String status;
}
