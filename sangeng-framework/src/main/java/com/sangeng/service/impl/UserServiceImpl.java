package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddUserDto;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domain.dto.UserListDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.entity.UserRole;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.UserInfoVo;
import com.sangeng.domain.vo.UserRoleVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.UserMapper;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-09 19:47:22
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        // 对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        // 对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        // 存入数据库
        save(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        Page<User> userPage = new Page<>();
        userPage.setCurrent(pageNum);
        userPage.setSize(pageSize);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userListDto.getUserName()),
                User::getUserName, userListDto.getUserName())
                .like(StringUtils.hasText(userListDto.getPhonenumber()),
                        User::getPhonenumber, userListDto.getPhonenumber())
                .like(StringUtils.hasText(userListDto.getStatus()),
                        User::getStatus, userListDto.getStatus());

        Page<User> page = page(userPage, queryWrapper);


        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        List<User> list = list();

        if (!StringUtils.hasText(addUserDto.getUserName())) {
            return new ResponseResult(403, "必须填写用户名！");
        }
        boolean match = list.stream().noneMatch(
                user -> user.getUserName().equals(addUserDto.getUserName()));
        if (!match) {
            return new ResponseResult(403, "用户名已存在！");
        }
//        List<User> list1 = list();
        boolean match1 = list.stream().anyMatch(
                new Predicate<User>() {
                    @Override
                    public boolean test(User user) {
                        if(user.getPhonenumber()!=null){
                            return user.getPhonenumber().equals(addUserDto.getPhonenumber());
                        }else{
                            return false;
                        }

                    }
                });
        if (match1) {
            return new ResponseResult(403, "手机号已存在！");
        }
        boolean match2 = list.stream().anyMatch(
                new Predicate<User>() {
                    @Override
                    public boolean test(User user) {
                        if (StringUtils.hasText(user.getEmail())) {
                            return user.getEmail().equals(addUserDto.getEmail());
                        }
                        return false;
                    }
                });
        if (match2) {
            return new ResponseResult(403, "邮箱已存在！");
        }
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);
        List<Long> roleIds = addUserDto.getRoleIds();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole(user.getId(), roleId);
            userRoleService.save(userRole);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long id) {
        if(SecurityUtils.getUserId()==id){
            return new ResponseResult(403,"不能删除当前操作的用户");
        }
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        userRoleService.remove(queryWrapper);

        removeById(id);
        return ResponseResult.okResult();
    }

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult showUser(Long id) {
        List<Role> roles = roleService.list();
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        ArrayList<Long> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        User user = getById(id);
        return ResponseResult.okResult(new UserRoleVo(roleIds,roles,user));
    }


    @Override
    public ResponseResult updateUser(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        updateById(user);

        Long userId = userDto.getId();
        List<Long> roleIds = userDto.getRoleIds();
        for (Long roleId : roleIds) {
            userRoleService.save(new UserRole(userId,roleId));
        }

        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);

        return count(queryWrapper) > 0;

    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);

        return count(queryWrapper) > 0;

    }
}

