package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.system.dao.SysMenuRoleDao;
import com.thyme.system.dao.SysRoleDao;
import com.thyme.system.entity.SysMenuRole;
import com.thyme.system.entity.SysRole;
import com.thyme.system.service.SysRoleService;
import com.thyme.system.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author thyme
 * @ClassName SysRoleServiceImpl
 * @Description TODO
 * @Date 2019/12/13 13:29
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleDao sysRoleDao;

    private final SysMenuRoleDao sysMenuRoleDao;

    @Override
    public SysRole findByUserId(String userId) {
        return sysRoleDao.findByUserId(userId);
    }

    @Override
    public SysRole findByRoleId(String roleId) {
        return sysRoleDao.findByRoleId(roleId);
    }

    @Override
    public IPage<SysRole> getAll(Page page) {
        return sysRoleDao.getAll(page);
    }

    @Override
    public SysRole getByName(String name) {
        return sysRoleDao.getByName(name);
    }

    @Override
    public String getById(String id) {
        return sysRoleDao.getById(id);
    }

    @Override
    public int deleteById(String id) {
        return sysRoleDao.deleteById(id);
    }

    @Override
    public int updateById(RoleVO roleVO) {

        Collection<GrantedAuthority> authorities = SecurityUtils.getCurrentUserToken().getAuthorities();
        String authority = "";
        for (GrantedAuthority g:authorities){
            authority = g.getAuthority();
        }
        if("ROLE_DEVELOPER".equals(roleVO.getAuthority()) && !"ROLE_DEVELOPER".equals(authority)){
            return 0;
        }
        sysMenuRoleDao.deleteByRoleId(roleVO.getId());
        for (String menuId : roleVO.getIds()){
            SysMenuRole sysMenuRole = new SysMenuRole(menuId, roleVO.getId());
            sysMenuRoleDao.addMenuRole(sysMenuRole);
        }
        SysRole sysRole = new SysRole();
        sysRole.setId(roleVO.getId());
        sysRole.setName(roleVO.getName());
        sysRole.setAuthority(roleVO.getAuthority());
        return sysRoleDao.updateById(sysRole);
    }

    @Override
    public int insert(SysRole sysRole) {
        return sysRoleDao.insert(sysRole);
    }

    @Override
    public List<String> getAllRoleName() {
        return sysRoleDao.getAllRoleName();
    }

    @Override
    public String getIdByName(String name) {
        return sysRoleDao.getIdByName(name);
    }
}
