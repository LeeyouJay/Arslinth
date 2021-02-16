package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.thyme.common.utils.MoneyUtils;
import com.thyme.system.dao.RemittanceDao;
import com.thyme.system.dao.ReturnedDao;
import com.thyme.system.entity.bussiness.Remittance;
import com.thyme.system.entity.bussiness.Returned;
import com.thyme.system.service.RemittanceService;
import com.thyme.system.vo.RemittanceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Arslinth
 * @ClassName RemittanceServiceImpl
 * @Description TODO
 * @Date 2020/6/19
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RemittanceServiceImpl implements RemittanceService {

    private final RemittanceDao remittanceDao;

    private final ReturnedDao returnedDao;

    @Override
    public List<RemittanceVO> getRemittances(String pcpName) {
        List<Returned> returnedList = returnedDao.getReturnedListByPcpName(pcpName);
        List<RemittanceVO> remittances = remittanceDao.getRemittances(pcpName);
        remittances.stream().map(reVO->{
            Double totalCost = returnedList.stream().filter(returned -> reVO.getPcpName().equals(returned.getPcpName()))
                    .map(Returned::getTotalCost)
                    .reduce(0.0, MoneyUtils::add);
            reVO.setReturnedCost(totalCost);
            return reVO;
        }).collect(Collectors.toList());

        return remittances;
    }

    public int addRemittance(Remittance remittance){
        return remittanceDao.insert(remittance);
    }

    @Override
    public int updateByPrincipalId(Remittance remittance) {
        return remittanceDao.updateByPrincipalId(remittance);
    }

    @Override
    public int updateDebt(Remittance remittance) {
        return remittanceDao.updateDebt(remittance);
    }

    @Override
    public int updateRemittanceById(Remittance remittance) {
        return remittanceDao.updateById(remittance);
    }

    @Override
    public Remittance findRemittanceById(String id) {
        return remittanceDao.selectById(id);
    }

    @Override
    public Remittance findByPrincipalId(String id) {
        QueryWrapper<Remittance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("principal_id",id);
        return remittanceDao.selectOne(queryWrapper);
    }

    @Override
    public int deleteByPrincipalId(String id) {
        QueryWrapper<Remittance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("principal_id",id);
        return remittanceDao.delete(queryWrapper);
    }
}
