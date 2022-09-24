package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZayneChen
 * @date 2022年09月21日 16:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private String menuName;
    private String status;
}
