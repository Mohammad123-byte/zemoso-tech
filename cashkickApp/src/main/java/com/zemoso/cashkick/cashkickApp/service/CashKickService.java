package com.zemoso.cashkick.cashkickApp.service;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.entities.CashKick;

import java.util.List;

public interface CashKickService {
    List<CashKickDTO> getAllCashKicks(Integer id);
    CashKickDTO addCashKick(CashKickDTO cashKickDTO);
}
