package com.thyme.system.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.base.Constants;

import com.thyme.common.utils.SecurityUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.entity.SysRole;
import com.thyme.system.entity.SysUser;
import com.thyme.system.entity.SysUserRole;
import com.thyme.system.service.SysRoleService;
import com.thyme.system.service.SysUserRoleService;
import com.thyme.system.service.SysUserService;
import com.thyme.system.service.UploadService;
import com.thyme.system.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cuiyating
 * @date 2020/1/2 20:45
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRestController {

    private final SysUserService userService;

    private final SysRoleService sysRoleService;

    private final SysUserRoleService sysUserRoleService;

    private final SysUserService sysUserService;

    private final UploadService uploadService;

    @GetMapping("/getUserInfo")
    public ApiResponse getUserInfo(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize,
                                   @RequestParam("searchName") String username) {
        JSONObject jsonObject = new JSONObject();
        List<UserVO> userList = new ArrayList<>(16);
        IPage<UserVO> sysUserList = userService.getAll(new Page(page, pageSize), username);
        for (UserVO userVO : sysUserList.getRecords()) {
            userVO.setPassword(null);
            //设置密码不可见
            userList.add(userVO);
        }
        jsonObject.put("total", sysUserList.getTotal());
        jsonObject.put("page", sysUserList.getCurrent());
        jsonObject.put("page_size", sysUserList.getSize());
        jsonObject.put("sysUserList", userList);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/deleteUser")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse deleteUser(@RequestParam("id") String id) {
        JSONObject jsonObject = new JSONObject();
        String username = SecurityUtils.getCurrentUserAuthentication().getName();
        SysUser sysUser = sysUserService.findByName(username);

        if (sysUser != null && sysUser.getId().equals(id)) {
            SysRole role = sysRoleService.findByUserId(sysUser.getId());
            if ("ROLE_DEVELOPER".equals(role.getAuthority()))
                return ApiResponse.fail("权限不足！");
            return ApiResponse.fail("不允许删除自己！");
        } else {
            sysUserRoleService.deleteByUserId(id);
            userService.deleteById(id);
            jsonObject.put("code", 200);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/updateUser")
    @ResponseBody
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ApiResponse updateRole(@RequestBody UserVO userVO) {
        AbstractAuthenticationToken token = SecurityUtils.getCurrentUserToken();
        String currentName = token.getName();
        SysUser userByName = sysUserService.findByName(userVO.getName());
        SysUser currentUser = sysUserService.findByName(currentName);
        if (userByName != null && !userByName.getId().equals(userVO.getId())) {
            return ApiResponse.fail("用户名重复！");
        }
//        SysUser sysUser = new SysUser(userVO.getId(), userVO.getName(), null, userVO.getNickName(), userVO.getSex(), userVO.getMobile(),
//                userVO.getEmail(), userVO.getBirthday(), userVO.getHobby(), userVO.getLiveAddress(), null, new Date());
        SysUser sysUser= SysUser.builder()
                .id(userVO.getId())
                .name(userVO.getName())
                .nickName(userVO.getNickName())
                .sex(userVO.getSex())
                .mobile(userVO.getMobile())
                .email(userVO.getEmail())
                .birthday(userVO.getBirthday())
                .hobby(userVO.getHobby())
                .liveAddress(userVO.getLiveAddress())
                .build();
        try {
            synchronized (this) {
                sysUserRoleService.deleteByUserId(userVO.getId());
                sysUserRoleService.insert(new SysUserRole(userVO.getId(), sysRoleService.getIdByName(userVO.getUserRole())));
            }
            userService.updateById(sysUser);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println(e);
            return ApiResponse.fail("更新失败！");
        }
        if (currentUser.getId().equals(userVO.getId()))
            return ApiResponse.ofMessage(201,"更新成功！");
        else
            return ApiResponse.success("更新成功！");
    }

    @PostMapping("/addUser")
    @ResponseBody
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ApiResponse addRole(@RequestBody UserVO userVO) {
        JSONObject jsonObject = new JSONObject();
        SysUser user = userService.findByName(userVO.getName());
        if (user == null) {
            //用户id
            String userId = UUIDUtils.getUUID();
            //角色id
            SysUserRole sysUserRole = new SysUserRole();

            sysUserRole.setRoleId(sysRoleService.getIdByName(userVO.getUserRole()));
            sysUserRole.setUserId(userId);
            sysUserRoleService.insert(sysUserRole);
//            SysUser sysUser = new SysUser(userId, userVO.getName(), new BCryptPasswordEncoder().encode(userVO.getPassword()), userVO.getNickName(), userVO.getSex(),
//                    userVO.getMobile(), userVO.getEmail(), userVO.getBirthday(), userVO.getHobby(), userVO.getLiveAddress(), new Date(), null);

            SysUser sysUser= SysUser.builder()
                    .id(userId)
                    .name(userVO.getName())
                    .password(new BCryptPasswordEncoder().encode(userVO.getPassword()))
                    .nickName(userVO.getNickName())
                    .sex(userVO.getSex())
                    .mobile(userVO.getMobile())
                    .email(userVO.getEmail())
                    .birthday(userVO.getBirthday())
                    .hobby(userVO.getHobby())
                    .liveAddress(userVO.getLiveAddress())
                    .build();
            if (userService.insert(sysUser) > 0) {
                jsonObject.put("code", 200);
            } else {
                jsonObject.put("code", 500);
            }
        } else {
            // 501 用户已存在
            jsonObject.put("code", 501);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/editPassword")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER','ROLE_MANAGER')")
    public ApiResponse editPassword(String id) {
        JSONObject jsonObject = new JSONObject();
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(Constants.CZMM));
        try {
            if (userService.updateById(sysUser) > 0) {
                jsonObject.put("code", 200);
            }
        } catch (Exception e) {
            jsonObject.put("code", 500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getAllRoleName")
    public ApiResponse getAllRoleName() {
        JSONObject jsonObject = new JSONObject();
        List<String> allRoleName = sysRoleService.getAllRoleName();
        jsonObject.put("allRoleName", allRoleName);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/editUser")
    @ResponseBody
    public ApiResponse editUser(@RequestBody UserVO userVO) {
        JSONObject jsonObject = new JSONObject();
        SysUser sysUser = new SysUser();
        sysUser.setId(userVO.getId());
        sysUser.setName(userVO.getName());
        sysUser.setNickName(userVO.getNickName());
        sysUser.setEmail(userVO.getEmail());
        sysUser.setSex(userVO.getSex());
        sysUser.setMobile(userVO.getMobile());
        sysUser.setBirthday(userVO.getBirthday());
        sysUser.setHobby(userVO.getHobby());
        sysUser.setLiveAddress(userVO.getLiveAddress());
        if (userService.updateById(sysUser) > 0) {
            jsonObject.put("code", 200);
            return ApiResponse.ofSuccess(jsonObject);
        } else {
            return ApiResponse.fail("更新基本资料失败");
        }
    }

    @PostMapping("/updateAvatar")
    @ResponseBody
    public ApiResponse updateAvatar(MultipartFile file){
        AbstractAuthenticationToken token = SecurityUtils.getCurrentUserToken();
        String username = token.getName();
        JSONObject jsonObject = new JSONObject();
        try {
            String path = uploadService.uploadImg(file, username);
            path = path.substring(1,path.length());
            int i = userService.updateAvatarByName(username, path);
            jsonObject.put("src",path);
            jsonObject.put("msg","头像修改成功！");
            if (i==1){
                return ApiResponse.ofSuccess(jsonObject);
            }else{
                return ApiResponse.fail("更新头像失败！");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail("更新出现异常！");
        }
    }

}
