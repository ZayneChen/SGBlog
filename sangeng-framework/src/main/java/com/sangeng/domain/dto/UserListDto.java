package com.sangeng.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZayneChen
 * @date 2022年09月23日 9:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {
    private String userName;
    private String phonenumber;
    private String status;
}
