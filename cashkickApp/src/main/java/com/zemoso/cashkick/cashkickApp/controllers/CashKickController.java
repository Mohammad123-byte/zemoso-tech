package com.zemoso.cashkick.cashkickApp.controllers;

import com.zemoso.cashkick.cashkickApp.dtos.CashKickDTO;
import com.zemoso.cashkick.cashkickApp.service.CashKickService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cashkicks")
@Slf4j
public class CashKickController {

    private final CashKickService cashKickService;

    public CashKickController(CashKickService cashKickService){
        this.cashKickService = cashKickService;
    }

    @PostMapping
    public ResponseEntity<CashKickDTO> createCashkick(@Valid @RequestBody CashKickDTO cashKickDTO)
    {
        log.info("Starting createCashKick() in CashKickController :: ");
        CashKickDTO createdCashkick = cashKickService.addCashKick(cashKickDTO);
        return new ResponseEntity<>(createdCashkick,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CashKickDTO>> getAllCashkicks(@RequestParam Integer id){
        log.info("Starting getAllCashkicks in CashKickController :: ");
        List<CashKickDTO> cashKickDtoList =  cashKickService.getAllCashKicks(id);
        return new ResponseEntity<>(cashKickDtoList,HttpStatus.OK);
    }


}
