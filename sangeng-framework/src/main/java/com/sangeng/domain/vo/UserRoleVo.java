package com.sangeng.domain.vo;

import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZayneChen
 * @date 2022年09月23日 14:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private User user;
}
