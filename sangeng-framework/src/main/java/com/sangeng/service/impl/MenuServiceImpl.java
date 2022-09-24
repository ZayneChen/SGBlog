package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.MenuDto;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-09-18 21:33:38
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        // 判断是否是管理员
        if (SecurityUtils.isAdmin()){
            // 如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
            // 否则 返回当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeBuUserId(userId);
        }
        // 构建tree
        // 先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);

        return menuTree;
    }

    @Override
    public ResponseResult listMenu(MenuDto menuDto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(StringUtils.hasText(menuDto.getMenuName()),Menu::getMenuName,menuDto.getMenuName());
            queryWrapper.like(menuDto.getStatus()!=null,Menu::getStatus,menuDto.getStatus());

        queryWrapper.orderByAsc(Menu::getOrderNum);

        List<Menu> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult deleteConditional(Long menuId) {
        // 遍历所有的menu看一下是否有 parentId=menuId 如果有则删除失败，没有则删除成功
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        List<Menu> menus = list(queryWrapper);
        System.out.println(menus);
        if(menus.size()==0){
            removeById(menuId);
            return ResponseResult.okResult();
        }else {
            ResponseResult responseResult = new ResponseResult(500, "存在子菜单不允许删除");
            return responseResult;
        }

    }

    /**
     * 先查询 parentId为0的 menu集合
     * 再根据表中parentId等于menu集合中id查询
     * 注意前端格式要求
     * @return
     */
    @Override
    public ResponseResult treeMenu() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,0L);
        List<Menu> menus = list(queryWrapper);

        ArrayList<MenuVo> menuVos = new ArrayList<>();

        for (Menu menu : menus) {
            MenuVo menuVo = new MenuVo(null, menu.getId(),
                    menu.getMenuName(), menu.getParentId());
            menuVos.add(menuVo);
        }
        /*for (MenuVo menuVo : menuVos) {
            LambdaQueryWrapper<Menu> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Menu::getParentId,menuVo.getId());

            List<Menu> children = list(queryWrapper1);
            ArrayList<MenuVo> childrenVos = new ArrayList<>();
            for (Menu menu : children) {
                MenuVo menuVo1 = new MenuVo(null, menu.getId(),
                        menu.getMenuName(), menu.getParentId());
                childrenVos.add(menuVo1);
            }

            menuVo.setChildren(childrenVos);
            // test
            List<MenuVo> children1 = menuVo.getChildren();
            for (MenuVo innerChild : children1) {
                LambdaQueryWrapper<Menu> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(Menu::getParentId,innerChild.getId());

                List<Menu> lists = list(queryWrapper2);

                ArrayList<MenuVo> innerChildrenVos = new ArrayList<>();
                for (Menu menu : lists) {
                    MenuVo menuVo1 = new MenuVo(null, menu.getId(),
                            menu.getMenuName(), menu.getParentId());
                    innerChildrenVos.add(menuVo1);
                }
                innerChild.setChildren(innerChildrenVos);
            }
        }*/
        // test
        queryChildren(menuVos);

        return ResponseResult.okResult(menuVos);
    }

    private void queryChildren(List<MenuVo> menuVos){
        for (MenuVo menuVo : menuVos) {
            LambdaQueryWrapper<Menu> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Menu::getParentId,menuVo.getId());

            List<Menu> children = list(queryWrapper1);
            ArrayList<MenuVo> childrenVos = new ArrayList<>();
            for (Menu menu : children) {
                MenuVo menuVo1 = new MenuVo(null, menu.getId(),
                        menu.getMenuName(), menu.getParentId());
                childrenVos.add(menuVo1);
            }

            menuVo.setChildren(childrenVos);
            List<MenuVo> children1 = menuVo.getChildren();
            if (children1.size()>0){
                queryChildren(children1);
            }
        }
    }
    @Override
    public List<MenuVo> getAllMenu(){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,0L);
        List<Menu> menus = list(queryWrapper);

        ArrayList<MenuVo> menuVos = new ArrayList<>();

        for (Menu menu : menus) {
            MenuVo menuVo = new MenuVo(null, menu.getId(),
                    menu.getMenuName(), menu.getParentId());
            menuVos.add(menuVo);
        }
        queryChildren(menuVos);
        return menuVos;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

