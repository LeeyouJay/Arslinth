package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.dao.UpdateLogDao;
import com.thyme.system.entity.bussiness.UpdateLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Arslinth
 * @ClassName UpdateLogServiceImpl
 * @Description TODO
 * @Date 2020/11/10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateLogServiceImpl {

    private final UpdateLogDao updateLogDao;

    public List<UpdateLog> getLogList() {
        QueryWrapper<UpdateLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return updateLogDao.selectList(wrapper);
    }

    public int addLog(UpdateLog updateLog) {
        return updateLogDao.insert(updateLog);
    }

    public int updateLog(UpdateLog updateLog) {
        return updateLogDao.updateById(updateLog);
    }

    public UpdateLog findById(String id) {
        return updateLogDao.selectById(id);
    }
}
