package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.system.dao.SysUserDao;
import com.thyme.system.entity.SysUser;
import com.thyme.system.service.SysUserService;
import com.thyme.system.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author thyme
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Date 2019/12/11 10:30
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysUserServiceImpl implements SysUserService {

    private final SysUserDao sysUserDao;

    @Override
    public SysUser findByName(String name) {
        return sysUserDao.findByName(name);
    }

    @Override
    public IPage<UserVO> getAll(Page page,String username) {
        return sysUserDao.getAll(page,username);
    }

    @Override
    public SysUser getById(String id) {
        return sysUserDao.getById(id);
    }

    @Override
    public int deleteById(String id) {
        return sysUserDao.deleteById(id);
    }

    @Override
    public int updateById(SysUser sysUser) {
        return sysUserDao.updateById(sysUser);
    }

    @Override
    public int insert(SysUser sysUser) {
        return sysUserDao.insert(sysUser);
    }

    @Override
    public int updatePasswordById(String password, String id) {
        return sysUserDao.updatePasswordById(password,id);
    }

    @Override
    public int updateAvatarByName(String username, String path) {
        return sysUserDao.updateAvatar(username,path);
    }
}
