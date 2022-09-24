package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.*;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.Role;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZayneChen
 * @date 2022年09月21日 15:45
 */
@RestController
public class SystemController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/system/menu/list")
    public ResponseResult listMenu(MenuDto menuDto){
        /*LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> list = menuService.list(queryWrapper);*/

        return menuService.listMenu(menuDto);
    }

    @PostMapping("/system/menu")
    public ResponseResult addMenu(@RequestBody Menu menu){

        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/system/menu/{id}")
    public ResponseResult showMenu(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        return ResponseResult.okResult(menu);
    }

    @PutMapping("/system/menu")
    public ResponseResult updateMenu(@RequestBody Menu menu){
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/system/menu/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        return menuService.deleteConditional(menuId);
    }

    @GetMapping("/system/role/list")
    public ResponseResult showRoles(Integer pageNum, Integer pageSize, Role role){
        return roleService.showRoles(pageNum,pageSize,role);
    }

    @PutMapping("/system/role/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeRoleDto changeRoleDto){
        return roleService.changeStatus(changeRoleDto);
    }

    @GetMapping("/system/menu/treeselect")
    public ResponseResult treeSelect(){

        return menuService.treeMenu();
    }

    @PostMapping("/system/role")
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/system/role/{id}")
    public ResponseResult showRole(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return ResponseResult.okResult(role);
    }

    @GetMapping("/system/menu/roleMenuTreeselect/{id}")
    public ResponseResult showRoleMenu(@PathVariable Long id){
        return roleService.loadRoleMessage(id);
    }

    @PutMapping("/system/role")
    public ResponseResult updateRoleMessage(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRoleMessage(updateRoleDto);
    }

    @DeleteMapping("/system/role/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    @Autowired
    private UserService userService;

    @GetMapping("/system/user/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto){
        return userService.getUserList(pageNum,pageSize,userListDto);
    }

    @GetMapping("/system/role/listAllRole")
    public ResponseResult listAllRole(){
        return ResponseResult.okResult(roleService.list());
    }

    @PostMapping("/system/user")
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/system/user/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/system/user/{id}")
    public ResponseResult showUser(@PathVariable Long id){
        return userService.showUser(id);
    }

    @PutMapping("/system/user")
    public ResponseResult update(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }
}
