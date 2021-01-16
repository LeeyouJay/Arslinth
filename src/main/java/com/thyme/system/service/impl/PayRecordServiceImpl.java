package com.thyme.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thyme.common.utils.MoneyUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.config.exception.RollbackException;
import com.thyme.system.dao.PayRecordDao;
import com.thyme.system.dao.TicketDao;
import com.thyme.system.entity.bussiness.PayRecord;
import com.thyme.system.entity.bussiness.Remittance;
import com.thyme.system.entity.bussiness.Ticket;
import com.thyme.system.service.PayRecordService;
import com.thyme.system.service.RemittanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @author Arslinth
 * @ClassName PayRecordServiceImpl
 * @Description TODO
 * @Date 2020/6/19
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayRecordServiceImpl implements PayRecordService {

    private final PayRecordDao payRecordDao;

    private final RemittanceService remittanceService;

    private final TicketDao ticketDao;
    @Override
    public List<PayRecord> getByPrincipalId(String id) {
        QueryWrapper<PayRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("principal_id",id);
        return  payRecordDao.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int addPayRecord(PayRecord payRecord) {
        try {
            payRecord.setId(UUIDUtils.getUUID());

            Remittance remittance = remittanceService.findByPrincipalId(payRecord.getPrincipalId());
            if (remittance == null)
                throw new RollbackException("找不到对应的负责人付款记录！");

            remittance.setTotalPay(MoneyUtils.add(remittance.getTotalPay(),payRecord.getPay()));
            remittance.setDebt(MoneyUtils.add(remittance.getDebt(),payRecord.getPay()));

            remittanceService.updateDebt(remittance);

            return payRecordDao.insert(payRecord);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int updateRecords(PayRecord payRecord) {

        try {

            PayRecord record = payRecordDao.selectById(payRecord.getId());

            double v = MoneyUtils.sub(payRecord.getPay(), record.getPay());

            Remittance remittance = remittanceService.findByPrincipalId(payRecord.getPrincipalId());
            if (remittance == null)
                throw new RollbackException("找不到对应的负责人付款记录！");

            remittance.setTotalPay(MoneyUtils.add(remittance.getTotalPay(),v));
            remittance.setDebt(MoneyUtils.add(remittance.getDebt(),v));

            remittanceService.updateDebt(remittance);

            return payRecordDao.updateById(payRecord);
        } catch (RollbackException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }

    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public int deleteRecord(String id) {
        try {
            PayRecord payRecord = payRecordDao.selectById(id);
            Remittance remittance = remittanceService.findByPrincipalId(payRecord.getPrincipalId());

            if (remittance == null)
                throw new RollbackException("找不到对应的负责人付款记录！");

            remittance.setTotalPay(MoneyUtils.sub(remittance.getTotalPay(),payRecord.getPay()));
            remittance.setDebt(MoneyUtils.sub(remittance.getDebt(),payRecord.getPay()));
            remittanceService.updateDebt(remittance);
            return payRecordDao.deleteById(id);
        } catch (RollbackException e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
    }

    @Override
    public List<Ticket> getTickets(String principalId) {
        return ticketDao.getTicketsByPcpId(principalId);
    }

    @Override
    public int deleteTicket(String id) {
        return ticketDao.deleteById(id);
    }

    @Override
    public int addTicket(Ticket ticket) {
        return ticketDao.insert(ticket);
    }
}
