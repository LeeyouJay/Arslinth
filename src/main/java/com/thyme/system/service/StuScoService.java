package com.thyme.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.system.entity.SysUser;
import com.thyme.system.entity.stuInfo.StuScore;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/5/19
 */
public interface StuScoService {
    List<StuScore> selectAll();
}
