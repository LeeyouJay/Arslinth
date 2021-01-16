package com.thyme.system.service;

import com.thyme.system.entity.bussiness.PayRecord;
import com.thyme.system.entity.bussiness.Ticket;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/19
 */
public interface PayRecordService {

    List<PayRecord> getByPrincipalId(String id);

    int addPayRecord(PayRecord payRecord);

    int updateRecords(PayRecord payRecord);

    int deleteRecord(String id);

    List<Ticket> getTickets(String principalId);

    int deleteTicket(String id);

    int addTicket(Ticket ticket);
}
