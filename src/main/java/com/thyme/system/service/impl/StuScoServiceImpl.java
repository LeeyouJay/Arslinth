package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.system.dao.StuScoDao;
import com.thyme.system.entity.SysUser;
import com.thyme.system.entity.stuInfo.StuScore;
import com.thyme.system.service.StuScoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName StuScoServiceImpl
 * @Description TODO
 * @Date 2020/5/19
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StuScoServiceImpl implements StuScoService {

    private final StuScoDao stuScoDao;

    @Override
    public List<StuScore> selectAll() {
        return stuScoDao.selectList(new QueryWrapper<>());
    }
}
