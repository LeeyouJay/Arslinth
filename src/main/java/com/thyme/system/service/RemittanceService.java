package com.thyme.system.service;

import com.thyme.system.entity.bussiness.Remittance;
import com.thyme.system.vo.RemittanceVO;

import java.util.List;

/**
 * @author Arslinth
 * @Description TODO
 * @Date 2020/6/19
 */
public interface RemittanceService {

    List<RemittanceVO> getRemittances(String pcpName);

    int addRemittance(Remittance remittance);

    int updateRemittanceById(Remittance remittance);

    int updateByPrincipalId(Remittance remittance);

    int updateDebt(Remittance remittance);

    int deleteByPrincipalId(String id);

    Remittance findRemittanceById(String id);

    Remittance findByPrincipalId(String id);
}
