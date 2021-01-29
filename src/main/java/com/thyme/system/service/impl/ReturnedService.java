package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thyme.system.dao.ReturnedDao;
import com.thyme.system.entity.bussiness.Returned;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReturnedService {
    private final ReturnedDao returnedDao;

    public List<Returned> getReturnedList(){
        QueryWrapper<Returned> wrapper = new QueryWrapper<>();
        return returnedDao.selectList(wrapper);
    }
}
