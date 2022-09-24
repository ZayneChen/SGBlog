package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddRoleDto;
import com.sangeng.domain.dto.ChangeRoleDto;
import com.sangeng.domain.dto.UpdateRoleDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.RoleMenuTreeVo;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-09-18 21:46:43
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {

        // 判断是否是管理员 如果是返回集合中只需要有admin
        if(id==1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        // 否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult showRoles(Integer pageNum, Integer pageSize, Role role) {
        Page<Role> rolePage = new Page<>();
        rolePage.setSize(pageSize);
        rolePage.setCurrent(pageNum);

        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getRoleSort);
        queryWrapper.like(StringUtils.hasText(role.getRoleName()),Role::getRoleName,role.getRoleName());
        queryWrapper.like(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getStatus());

        page(rolePage, queryWrapper);

        // 封装数据返回
        PageVo pageVo = new PageVo(rolePage.getRecords(), rolePage.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult changeStatus(ChangeRoleDto changeRoleDto) {
        Role role = getById(changeRoleDto.getRoleId());
        role.setStatus(changeRoleDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = new Role();
        role.setRoleName(addRoleDto.getRoleName());
        role.setRoleKey(addRoleDto.getRoleKey());
        role.setRoleSort(addRoleDto.getRoleSort());
        role.setStatus(addRoleDto.getStatus());
        role.setRemark(addRoleDto.getRemark());

        save(role);

        List<Long> menuIds = addRoleDto.getMenuIds();
        for (Long menuId : menuIds) {

            roleMenuService.save(new RoleMenu(role.getId(),menuId));
        }

//        System.out.println(role.getId());


        return ResponseResult.okResult();
    }

    @Autowired
    private MenuService menuService;

    @Override
    public ResponseResult loadRoleMessage(Long id) {
        List<MenuVo> allMenu = menuService.getAllMenu();
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> list = roleMenuService.list(queryWrapper);
        ArrayList<Long> ids = new ArrayList<>();
        for (RoleMenu roleMenu : list) {
            ids.add(roleMenu.getMenuId());
        }
        System.out.println(ids);
        RoleMenuTreeVo roleMenuTreeVo = new RoleMenuTreeVo(allMenu, ids);
        return ResponseResult.okResult(roleMenuTreeVo);
    }

    @Override
    public ResponseResult updateRoleMessage(UpdateRoleDto updateRoleDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        System.out.println(role);
        updateById(role);
        List<Long> menuIds = updateRoleDto.getMenuIds();
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,updateRoleDto.getId());
        roleMenuService.remove(queryWrapper);
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(updateRoleDto.getId(), menuId);
            roleMenuService.save(roleMenu);
        }
        return ResponseResult.okResult();
    }


}

