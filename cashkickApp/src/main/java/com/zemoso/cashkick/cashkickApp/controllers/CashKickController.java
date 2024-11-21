package com.zemoso.cashkick.cashkickApp.controllers;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.entities.CashKick;
import com.zemoso.cashkick.cashkickApp.service.CashKickService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cashkicks")
public class CashKickController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashKickController.class);

    @Autowired
    private CashKickService cashKickService;

    @PostMapping
    public ResponseEntity<CashKickDTO> createCashkick(@Valid @RequestBody CashKickDTO cashKickDTO)
    {
        LOGGER.info("Starting createCashKick() in CashKickController :: ");
        CashKickDTO createdCashkick = cashKickService.addCashKick(cashKickDTO);
        return new ResponseEntity<>(createdCashkick,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CashKickDTO>> getAllCashkicks(@RequestParam Integer userId){
        List<CashKickDTO> cashKickDtoList =  cashKickService.getAllCashKicks(userId);
        return new ResponseEntity<>(cashKickDtoList,HttpStatus.OK);
    }


}
